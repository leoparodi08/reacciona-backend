package ar.edu.udemm.reacciona.monitoring;

import ar.edu.udemm.reacciona.progress.ModuleProgress;
import ar.edu.udemm.reacciona.progress.ModuleProgressRepository;
import ar.edu.udemm.reacciona.progress.ActivityAttempt;
import ar.edu.udemm.reacciona.progress.ActivityAttemptRepository;
import ar.edu.udemm.reacciona.users.Usuario;
import ar.edu.udemm.reacciona.users.UsuarioService;
import ar.edu.udemm.reacciona.users.UsuarioRepository;
import ar.edu.udemm.reacciona.modules.Modulo;
import ar.edu.udemm.reacciona.modules.ModuloRepository;
import ar.edu.udemm.reacciona.entity.Clase;
import ar.edu.udemm.reacciona.repository.ClaseRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/monitoring")
public class MonitoringController {

    private final UsuarioService usuarioService;
    private final UsuarioRepository usuarioRepository;
    private final ModuleProgressRepository moduleProgressRepository;
    private final ActivityAttemptRepository activityAttemptRepository;
    private final ModuloRepository moduloRepository;
    private final ClaseRepository claseRepository;

    public MonitoringController(
            UsuarioService usuarioService,
            UsuarioRepository usuarioRepository,
            ModuleProgressRepository moduleProgressRepository,
            ActivityAttemptRepository activityAttemptRepository,
            ModuloRepository moduloRepository,
            ClaseRepository claseRepository) {
        this.usuarioService = usuarioService;
        this.usuarioRepository = usuarioRepository;
        this.moduleProgressRepository = moduleProgressRepository;
        this.activityAttemptRepository = activityAttemptRepository;
        this.moduloRepository = moduloRepository;
        this.claseRepository = claseRepository;
    }

    /**
     * Obtiene las clases asignadas al profesor autenticado
     */
    @GetMapping("/classes")
    public ResponseEntity<List<Map<String, Object>>> getTeacherClasses() {
        Usuario currentUser = getCurrentUser();
        
        List<Clase> clases = claseRepository.findByIdDocenteCreador(currentUser.getId());
        
        List<Map<String, Object>> response = clases.stream().map(clase -> {
            Map<String, Object> claseInfo = new HashMap<>();
            claseInfo.put("id", clase.getId());
            claseInfo.put("name", clase.getNombreClase());
            claseInfo.put("description", clase.getDescripcion());
            claseInfo.put("studentCount", clase.getAlumnos() != null ? clase.getAlumnos().size() : 0);
            return claseInfo;
        }).collect(Collectors.toList());
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/group/{groupId}/statistics")
    public ResponseEntity<Map<String, Object>> getGroupStatistics(@PathVariable Long groupId) {
        Usuario currentUser = getCurrentUser();
        
        // Verificar que la clase pertenece al profesor autenticado
        if (!claseRepository.existsByIdAndDocenteCreador(groupId, currentUser.getId())) {
            return ResponseEntity.status(403).body(Map.of("error", "No tienes acceso a esta clase"));
        }
        
        Clase clase = claseRepository.findByIdAndDocenteCreador(groupId, currentUser.getId());
        List<Usuario> students = clase.getAlumnos();
        
        if (students.isEmpty()) {
            Map<String, Object> emptyStats = new HashMap<>();
            emptyStats.put("totalStudents", 0);
            emptyStats.put("activeStudents", 0);
            emptyStats.put("averageProgress", 0);
            emptyStats.put("averageScore", 0);
            return ResponseEntity.ok(emptyStats);
        }

        List<Map<String, Object>> studentStats = students.stream()
            .map(this::calculateStudentStats)
            .collect(Collectors.toList());

        // Calcular estadísticas grupales
        double avgProgress = studentStats.stream()
            .mapToDouble(s -> (Double) s.get("totalProgress"))
            .average().orElse(0.0);

        double avgScore = studentStats.stream()
            .mapToDouble(s -> (Double) s.get("averageScore"))
            .average().orElse(0.0);

        long activeStudents = studentStats.stream()
            .filter(s -> "active".equals(s.get("status")))
            .count();

        // Encontrar módulo más difícil
        Map<String, Object> difficultModule = findMostDifficultModule();

        Map<String, Object> response = new HashMap<>();
        response.put("groupId", groupId);
        response.put("groupName", clase.getNombreClase());
        response.put("totalStudents", students.size());
        response.put("activeStudents", (int) activeStudents);
        response.put("averageProgress", Math.round(avgProgress * 100.0) / 100.0);
        response.put("averageScore", Math.round(avgScore * 100.0) / 100.0);
        response.put("completionRate", calculateCompletionRate(studentStats));
        response.put("mostDifficultModule", difficultModule);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/group/{groupId}/students")
    public ResponseEntity<List<Map<String, Object>>> getGroupStudents(
            @PathVariable Long groupId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFrom,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateTo,
            @RequestParam(required = false) Long moduleId,
            @RequestParam(required = false) String status,
            @RequestParam(required = false, defaultValue = "name") String sortBy,
            @RequestParam(required = false, defaultValue = "asc") String sortOrder) {

        Usuario currentUser = getCurrentUser();
        
        // Verificar que la clase pertenece al profesor autenticado
        if (!claseRepository.existsByIdAndDocenteCreador(groupId, currentUser.getId())) {
            return ResponseEntity.status(403).body(List.of());
        }
        
        Clase clase = claseRepository.findByIdAndDocenteCreador(groupId, currentUser.getId());
        List<Usuario> students = clase.getAlumnos();
        
        List<Map<String, Object>> studentProgress = students.stream()
            .map(this::calculateStudentStats)
            .collect(Collectors.toList());

        // Aplicar filtros
        if (status != null && !"all".equals(status)) {
            studentProgress = studentProgress.stream()
                .filter(s -> status.equals(s.get("status")))
                .collect(Collectors.toList());
        }

        // Aplicar ordenamiento
        Comparator<Map<String, Object>> comparator = getComparator(sortBy);
        if ("desc".equals(sortOrder)) {
            comparator = comparator.reversed();
        }
        studentProgress.sort(comparator);

        return ResponseEntity.ok(studentProgress);
    }

    @GetMapping("/student/{studentId}/details")
    public ResponseEntity<Map<String, Object>> getStudentDetails(@PathVariable Long studentId) {
        Usuario student = usuarioRepository.findById(studentId)
            .orElseThrow(() -> new RuntimeException("Estudiante no encontrado"));

        Map<String, Object> details = new HashMap<>();
        
        // Información básica del estudiante
        Map<String, Object> studentInfo = new HashMap<>();
        studentInfo.put("id", student.getId());
        studentInfo.put("nombre", student.getNombre());
        studentInfo.put("email", student.getEmail());
        details.put("student", studentInfo);

        // Progreso general
        Map<String, Object> overallProgress = calculateDetailedStudentStats(student);
        details.put("overallProgress", overallProgress);

        // Progreso por módulo
        List<Map<String, Object>> moduleProgress = getModuleProgressForStudent(student);
        details.put("moduleProgress", moduleProgress);

        // Actividad reciente
        List<Map<String, Object>> recentActivity = getRecentActivityForStudent(student);
        details.put("recentActivity", recentActivity);

        // Logros (por ahora vacío, se implementará cuando esté el sistema de logros)
        details.put("achievements", new ArrayList<>());

        return ResponseEntity.ok(details);
    }

    @GetMapping("/group/{groupId}/export")
    public ResponseEntity<Map<String, Object>> exportGroupData(
            @PathVariable Long groupId,
            @RequestParam(required = false, defaultValue = "summary") String type) {
        
        Usuario currentUser = getCurrentUser();
        
        // Verificar que la clase pertenece al profesor autenticado
        if (!claseRepository.existsByIdAndDocenteCreador(groupId, currentUser.getId())) {
            return ResponseEntity.status(403).body(Map.of("error", "No tienes acceso a esta clase"));
        }
        
        // Obtener solo los estudiantes de la clase específica
        Clase clase = claseRepository.findByIdAndDocenteCreador(groupId, currentUser.getId());
        List<Usuario> students = clase.getAlumnos();
        
        List<Map<String, Object>> exportData = students.stream()
            .map(student -> {
                Map<String, Object> stats = calculateStudentStats(student);
                if ("detailed".equals(type)) {
                    // Agregar información detallada para exportación completa
                    stats.put("moduleProgress", getModuleProgressForStudent(student));
                    stats.put("recentActivity", getRecentActivityForStudent(student));
                }
                return stats;
            })
            .collect(Collectors.toList());

        Map<String, Object> response = new HashMap<>();
        response.put("groupId", groupId);
        response.put("groupName", clase.getNombreClase());
        response.put("exportDate", LocalDateTime.now().toString());
        response.put("students", exportData);
        response.put("summary", calculateGroupSummaryFromStudents(exportData));

        return ResponseEntity.ok(response);
    }

    private Map<String, Object> calculateStudentStats(Usuario student) {
        Map<String, Object> stats = new HashMap<>();
        
        List<ModuleProgress> progress = moduleProgressRepository.findByUsuario(student);
        List<Modulo> allModules = moduloRepository.findAll();
        
        // Calcular progreso total
        double totalProgress = progress.stream()
            .mapToDouble(ModuleProgress::getPorcentaje)
            .average().orElse(0.0);

        // Calcular puntaje promedio
        double averageScore = progress.stream()
            .filter(p -> p.getPuntajeTotal() > 0)
            .mapToDouble(p -> (double) p.getPuntajeTotal() / p.getPasosCompletados())
            .average().orElse(0.0);

        // Última actividad
        ActivityAttempt lastAttempt = activityAttemptRepository
            .findTop1ByUsuarioOrderByFechaIntentoDesc(student);
        
        String lastActivity = lastAttempt != null 
            ? calculateTimeAgo(lastAttempt.getFechaIntento())
            : "Sin actividad";

        // Determinar status
        String status = determineStudentStatus(Optional.ofNullable(lastAttempt), totalProgress);

        stats.put("id", student.getId());
        stats.put("nombre", student.getNombre());
        stats.put("email", student.getEmail());
        stats.put("lastActivity", lastActivity);
        stats.put("totalProgress", Math.round(totalProgress * 100.0) / 100.0);
        stats.put("averageScore", Math.round(averageScore * 100.0) / 100.0);
        stats.put("completedModules", (int) progress.stream().filter(p -> p.getPorcentaje() >= 100).count());
        stats.put("totalModules", allModules.size());
        stats.put("achievementsCount", 0); // Temporal
        stats.put("status", status);

        return stats;
    }

    private Map<String, Object> calculateDetailedStudentStats(Usuario student) {
        List<ModuleProgress> progress = moduleProgressRepository.findByUsuario(student);
        List<Modulo> allModules = moduloRepository.findAll();
        
        double totalProgress = progress.stream()
            .mapToDouble(ModuleProgress::getPorcentaje)
            .average().orElse(0.0);

        int totalScore = progress.stream()
            .mapToInt(ModuleProgress::getPuntajeTotal)
            .sum();

        double averageScore = progress.stream()
            .filter(p -> p.getPuntajeTotal() > 0 && p.getPasosCompletados() > 0)
            .mapToDouble(p -> (double) p.getPuntajeTotal() / p.getPasosCompletados())
            .average().orElse(0.0);

        Map<String, Object> overall = new HashMap<>();
        overall.put("totalProgress", Math.round(totalProgress * 100.0) / 100.0);
        overall.put("completedModules", (int) progress.stream().filter(p -> p.getPorcentaje() >= 100).count());
        overall.put("totalModules", allModules.size());
        overall.put("totalScore", totalScore);
        overall.put("averageScore", Math.round(averageScore * 100.0) / 100.0);
        overall.put("totalTimeSpent", 0); // Se implementará cuando se trackee tiempo
        overall.put("achievementsCount", 0); // Temporal

        return overall;
    }

    private List<Map<String, Object>> getModuleProgressForStudent(Usuario student) {
        List<ModuleProgress> progress = moduleProgressRepository.findByUsuario(student);
        
        return progress.stream().map(p -> {
            Map<String, Object> moduleInfo = new HashMap<>();
            moduleInfo.put("moduleId", p.getModulo().getId());
            moduleInfo.put("titulo", p.getModulo().getTitulo());
            moduleInfo.put("tipoEmergencia", p.getModulo().getTipoEmergencia());
            moduleInfo.put("status", determineModuleStatus(p));
            moduleInfo.put("pasosCompletados", p.getPasosCompletados());
            moduleInfo.put("pasosTotales", p.getPasosTotales());
            moduleInfo.put("puntajeTotal", p.getPuntajeTotal());
            moduleInfo.put("porcentaje", p.getPorcentaje());
            moduleInfo.put("timeSpent", 0); // Temporal
            moduleInfo.put("lastAccessed", p.getFechaActualizacion() != null ? 
                p.getFechaActualizacion().toString() : null);
            moduleInfo.put("attempts", countAttemptsForModule(student, p.getModulo().getId()));
            return moduleInfo;
        }).collect(Collectors.toList());
    }

    private List<Map<String, Object>> getRecentActivityForStudent(Usuario student) {
        List<ActivityAttempt> attempts = activityAttemptRepository
            .findTop10ByUsuarioOrderByFechaIntentoDesc(student);

        return attempts.stream().map(attempt -> {
            Map<String, Object> activity = new HashMap<>();
            activity.put("id", attempt.getId());
            activity.put("tipo", "STEP_COMPLETE");
            activity.put("descripcion", "Completó paso de simulación");
            activity.put("fecha", attempt.getFechaIntento().toString());
            activity.put("puntajeObtenido", attempt.getPuntajeOtorgado());
            activity.put("moduloTitulo", attempt.getPasoSimulacion().getContenido().getModulo().getTitulo());
            return activity;
        }).collect(Collectors.toList());
    }

    private String determineModuleStatus(ModuleProgress progress) {
        if (progress.getPorcentaje() >= 100) return "COMPLETED";
        if (progress.getPorcentaje() > 0) return "IN_PROGRESS";
        return "NOT_STARTED";
    }

    private int countAttemptsForModule(Usuario student, Long moduleId) {
        return activityAttemptRepository.countByUsuarioAndPasoSimulacion_Contenido_Modulo_Id(student, moduleId);
    }

    private Map<String, Object> findMostDifficultModule() {
        List<Modulo> modules = moduloRepository.findAll();
        Map<String, Object> difficult = new HashMap<>();
        
        if (!modules.isEmpty()) {
            // Por simplicidad, tomar el primer módulo como el más difícil
            Modulo module = modules.get(0);
            difficult.put("id", module.getId());
            difficult.put("titulo", module.getTitulo());
            difficult.put("averageScore", 65.0); // Temporal
            difficult.put("completionRate", 0.7); // Temporal
        }
        
        return difficult;
    }

    private double calculateCompletionRate(List<Map<String, Object>> studentStats) {
        if (studentStats.isEmpty()) return 0.0;
        
        long completed = studentStats.stream()
            .filter(s -> (Double) s.get("totalProgress") >= 100)
            .count();
        
        return Math.round((double) completed / studentStats.size() * 100.0) / 100.0;
    }

    private String determineStudentStatus(Optional<ActivityAttempt> lastAttempt, double totalProgress) {
        if (lastAttempt.isEmpty()) return "inactive";
        
        LocalDateTime lastActivity = lastAttempt.get().getFechaIntento();
        LocalDateTime now = LocalDateTime.now();
        
        if (lastActivity.isAfter(now.minusDays(3))) {
            return "active";
        } else if (totalProgress < 30 || lastActivity.isBefore(now.minusDays(7))) {
            return "at-risk";
        } else {
            return "inactive";
        }
    }

    private String calculateTimeAgo(LocalDateTime dateTime) {
        LocalDateTime now = LocalDateTime.now();
        long minutes = java.time.Duration.between(dateTime, now).toMinutes();
        
        if (minutes < 60) {
            return "hace " + minutes + " minutos";
        } else if (minutes < 1440) { // 24 horas
            return "hace " + (minutes / 60) + " horas";
        } else {
            long days = minutes / 1440;
            if (days == 1) return "hace 1 día";
            return "hace " + days + " días";
        }
    }

    private Map<String, Object> calculateGroupSummaryFromStudents(List<Map<String, Object>> studentData) {
        Map<String, Object> summary = new HashMap<>();
        
        if (studentData.isEmpty()) {
            summary.put("totalStudents", 0);
            summary.put("averageProgress", 0.0);
            summary.put("averageScore", 0.0);
            summary.put("completionRate", 0.0);
            return summary;
        }

        double avgProgress = studentData.stream()
            .mapToDouble(s -> (Double) s.get("totalProgress"))
            .average().orElse(0.0);

        double avgScore = studentData.stream()
            .mapToDouble(s -> (Double) s.get("averageScore"))
            .average().orElse(0.0);

        long completedStudents = studentData.stream()
            .filter(s -> (Double) s.get("totalProgress") >= 100.0)
            .count();

        double completionRate = (double) completedStudents / studentData.size() * 100.0;

        summary.put("totalStudents", studentData.size());
        summary.put("averageProgress", Math.round(avgProgress * 100.0) / 100.0);
        summary.put("averageScore", Math.round(avgScore * 100.0) / 100.0);
        summary.put("completionRate", Math.round(completionRate * 100.0) / 100.0);

        return summary;
    }

    private Comparator<Map<String, Object>> getComparator(String sortBy) {
        switch (sortBy) {
            case "progress":
                return Comparator.comparing(s -> (Double) s.get("totalProgress"));
            case "score":
                return Comparator.comparing(s -> (Double) s.get("averageScore"));
            case "lastActivity":
                return Comparator.comparing(s -> (String) s.get("lastActivity"));
            default:
                return Comparator.comparing(s -> (String) s.get("nombre"));
        }
    }

    /**
     * Obtiene el usuario actualmente autenticado
     */
    private Usuario getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return usuarioRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("Usuario autenticado no encontrado"));
    }
}