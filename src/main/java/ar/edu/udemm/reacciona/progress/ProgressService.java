package ar.edu.udemm.reacciona.progress;

import ar.edu.udemm.reacciona.entity.PasoSimulacion;
import ar.edu.udemm.reacciona.modules.Modulo;
import ar.edu.udemm.reacciona.modules.ModuloRepository;
import ar.edu.udemm.reacciona.progress.dto.AchievementDto;
import ar.edu.udemm.reacciona.progress.dto.ModuleProgressDto;
import ar.edu.udemm.reacciona.progress.dto.ProgressSummaryDto;
import ar.edu.udemm.reacciona.repository.PasoSimulacionRepository;
import ar.edu.udemm.reacciona.users.Usuario;
import ar.edu.udemm.reacciona.users.UsuarioService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ar.edu.udemm.reacciona.repository.ContenidoRepository;
import ar.edu.udemm.reacciona.repository.ContenidoRevisadoRepository;
import ar.edu.udemm.reacciona.entity.Contenido;
import ar.edu.udemm.reacciona.entity.ContenidoRevisado;
import ar.edu.udemm.reacciona.entity.ContenidoRevisadoId;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProgressService {

    private final ModuloRepository moduloRepository;
    private final ModuleProgressRepository moduleProgressRepository;
    private final ActivityAttemptRepository activityAttemptRepository;
    private final AchievementRepository achievementRepository;
    private final UsuarioService usuarioService;
    private final PasoSimulacionRepository pasoSimulacionRepository;
    private final ContenidoRepository contenidoRepository;
    private final ContenidoRevisadoRepository contenidoRevisadoRepository;

    public ProgressService(ModuloRepository moduloRepository,
                           ModuleProgressRepository moduleProgressRepository,
                           ActivityAttemptRepository activityAttemptRepository,
                           AchievementRepository achievementRepository,
                           UsuarioService usuarioService,
                           PasoSimulacionRepository pasoSimulacionRepository,
                           ContenidoRepository contenidoRepository,
                           ContenidoRevisadoRepository contenidoRevisadoRepository) {
        this.moduloRepository = moduloRepository;
        this.moduleProgressRepository = moduleProgressRepository;
        this.activityAttemptRepository = activityAttemptRepository;
        this.achievementRepository = achievementRepository;
        this.usuarioService = usuarioService;
        this.pasoSimulacionRepository = pasoSimulacionRepository;
        this.contenidoRepository = contenidoRepository;
        this.contenidoRevisadoRepository = contenidoRevisadoRepository;
    }

    /**
     * Devuelve el estado de la simulación para un contenido concreto (primer contenido SIMULACION de un módulo o uno específico):
     * - pasoActualIndex: índice (0-based) del paso que debería mostrarse al reanudar.
     * - pasoRespondidoId: id del paso ya respondido (último) y la opción elegida si existe.
     */
    @Transactional(readOnly = true)
    public Map<String, Object> getSimulationState(Long contenidoId) {
        Usuario usuario = usuarioService.getAuthenticatedUser();
        Map<String, Object> result = new HashMap<>();
        ActivityAttempt last = activityAttemptRepository.findTop1ByUsuarioAndPasoSimulacion_Contenido_IdOrderByFechaIntentoDesc(usuario, contenidoId);
        if (last == null) {
            result.put("pasoActualIndex", 0);
            return result;
        }
        PasoSimulacion paso = last.getPasoSimulacion();
        // Calcular índice: ordenar pasos del contenido por 'orden'
        List<PasoSimulacion> pasos = paso.getContenido().getPasosSimulacion();
        pasos.sort(Comparator.comparing(PasoSimulacion::getOrden));
        int index = 0;
        for (int i = 0; i < pasos.size(); i++) {
            if (Objects.equals(pasos.get(i).getIdPaso(), paso.getIdPaso())) {
                index = i; break;
            }
        }
        result.put("pasoActualIndex", index);
        result.put("pasoRespondidoId", paso.getIdPaso());
        result.put("opcionSeleccionadaId", last.getOpcionSeleccionadaId());
        return result;
    }

    /** Reinicia la simulación: borra intentos y reajusta progreso de módulo afectado. */
    @Transactional
    public void resetSimulation(Long contenidoId) {
        Usuario usuario = usuarioService.getAuthenticatedUser();
        // Borrar intentos
    activityAttemptRepository.deleteByUsuarioAndPasoSimulacion_Contenido_Id(usuario, contenidoId);
        // Obtener módulo del contenido
        List<PasoSimulacion> pasos = pasoSimulacionRepository.findByContenidoId(contenidoId);
        if (pasos.isEmpty()) return; // nada que resetear
        Modulo modulo = pasos.get(0).getContenido().getModulo();
        // Obtener progreso
        ModuleProgress mp = moduleProgressRepository.findByUsuarioAndModulo(usuario, modulo)
                .orElseGet(() -> createInitialProgress(usuario, modulo));
        // Recalcular: contar intentos restantes (ninguno tras delete) => pasosCompletados = 0, puntajeTotal = 0
        mp.setPasosCompletados(0);
        mp.setPuntajeTotal(0);
        mp.setStatus(ModuleProgressStatus.NOT_STARTED);
        mp.setFechaComplecion(null);
        moduleProgressRepository.save(mp);
    }

    @Transactional
    public ProgressSummaryDto getSummary(LocalDate from, LocalDate to, String tipoEmergencia, Long moduleId) {
        Usuario usuario = usuarioService.getAuthenticatedUser();
        LocalDateTime fromDateTime = from != null ? from.atStartOfDay() : null;
        LocalDateTime toDateTime = to != null ? to.atTime(LocalTime.MAX) : null;

        // Obtener todos los módulos (posible filtro de tipo emergencia)
        List<Modulo> modulos = moduloRepository.findAll();
        if (tipoEmergencia != null && !tipoEmergencia.isBlank()) {
            modulos = modulos.stream()
                    .filter(m -> m.getTipoEmergencia() != null && m.getTipoEmergencia().name().equalsIgnoreCase(tipoEmergencia))
                    .toList();
        }
        if (moduleId != null) {
            modulos = modulos.stream().filter(m -> m.getId().equals(moduleId)).toList();
        }

        // Asegurar que existan registros de progreso para cada módulo
        for (Modulo modulo : modulos) {
            moduleProgressRepository.findByUsuarioAndModulo(usuario, modulo)
                    .orElseGet(() -> createInitialProgress(usuario, modulo));
        }

        List<ModuleProgress> progressList = fromDateTime == null && toDateTime == null
                ? moduleProgressRepository.findByUsuario(usuario)
                : moduleProgressRepository.findByUsuarioAndDateRange(usuario, fromDateTime, toDateTime);

        Map<Long, ModuleProgress> progressByModule = progressList.stream()
                .collect(Collectors.toMap(mp -> mp.getModulo().getId(), mp -> mp));

        List<ModuleProgressDto> moduleDtos = new ArrayList<>();
        int totalScore = 0;

        for (Modulo modulo : modulos) {
            ModuleProgress mp = progressByModule.get(modulo.getId());
            if (mp == null) continue; // filtrado por fecha puede excluir
            int porcentaje = mp.getPasosTotales() == 0 ? 0 : (int) Math.round((mp.getPasosCompletados() * 100.0) / mp.getPasosTotales());
            moduleDtos.add(new ModuleProgressDto(
                    modulo.getId(),
                    modulo.getTitulo(),
                    mp.getStatus(),
                    mp.getPasosCompletados(),
                    mp.getPasosTotales(),
                    mp.getPuntajeTotal(),
                    porcentaje
            ));
            totalScore += mp.getPuntajeTotal();
        }

        int totalModules = moduleDtos.size();
        int completed = (int) moduleDtos.stream().filter(m -> m.status() == ModuleProgressStatus.COMPLETED).count();
        int inProgress = (int) moduleDtos.stream().filter(m -> m.status() == ModuleProgressStatus.IN_PROGRESS).count();
        int notStarted = (int) moduleDtos.stream().filter(m -> m.status() == ModuleProgressStatus.NOT_STARTED).count();

        // Achievements
        List<AchievementDto> achievements = achievementRepository.findByUsuario(usuario).stream()
                .map(a -> new AchievementDto(a.getCodigo(), a.getNombre(), a.getDescripcion(), a.getIcono(), a.getFechaObtencion()))
                .toList();

        return new ProgressSummaryDto(totalModules, completed, inProgress, notStarted, totalScore, moduleDtos, achievements);
    }

    private ModuleProgress createInitialProgress(Usuario usuario, Modulo modulo) {
        // calcular pasos totales (sumar pasos de cada contenido)
        int pasosTotales = modulo.getContenidos().stream()
                .mapToInt(c -> c.getPasosSimulacion().size())
                .sum();
        ModuleProgress mp = new ModuleProgress(usuario, modulo, pasosTotales);
        return moduleProgressRepository.save(mp);
    }

    @Transactional
    public void registerAttempt(PasoSimulacion paso, boolean correcto, int puntaje, Long opcionSeleccionadaId) {
        Usuario usuario = usuarioService.getAuthenticatedUser();
        ActivityAttempt attempt = new ActivityAttempt(usuario, paso, ActivityType.SIMULATION_STEP, correcto, puntaje, opcionSeleccionadaId);
        activityAttemptRepository.save(attempt);

        // actualizar progreso del módulo
        Modulo modulo = paso.getContenido().getModulo();
        ModuleProgress mp = moduleProgressRepository.findByUsuarioAndModulo(usuario, modulo)
                .orElseGet(() -> createInitialProgress(usuario, modulo));

        if (correcto) {
            mp.setPasosCompletados(mp.getPasosCompletados() + 1);
        }
        mp.setPuntajeTotal(mp.getPuntajeTotal() + puntaje);
        // actualizar status
        if (mp.getPasosCompletados() >= mp.getPasosTotales() && mp.getPasosTotales() > 0) {
            mp.setStatus(ModuleProgressStatus.COMPLETED);
            mp.setFechaComplecion(LocalDateTime.now());
        } else if (mp.getPasosCompletados() > 0) {
            mp.setStatus(ModuleProgressStatus.IN_PROGRESS);
        }
        moduleProgressRepository.save(mp);

        checkAndGrantAchievements(usuario, mp);
    }

    private void checkAndGrantAchievements(Usuario usuario, ModuleProgress mp) {
        // Ejemplos simples de criterios (podrían externalizarse a tabla de configuraciones)
        if (mp.getPasosCompletados() == 1) {
            grantIfNotExists(usuario, "FIRST_STEP", "Primer Paso", "Completaste tu primer paso de simulación", "medal-bronze");
        }
        if (mp.getStatus() == ModuleProgressStatus.COMPLETED) {
            grantIfNotExists(usuario, "MODULE_COMPLETED_" + mp.getModulo().getId(), "Módulo completado", "Completaste el módulo " + mp.getModulo().getTitulo(), "medal-module");
        }
        Integer totalScore = activityAttemptRepository.sumPuntajeByUsuario(usuario);
        if (totalScore != null && totalScore >= 1000) {
            grantIfNotExists(usuario, "SCORE_1000", "Experto", "Alcanzaste 1000 puntos de experiencia", "medal-gold");
        }
    }

    private void grantIfNotExists(Usuario usuario, String codigo, String nombre, String descripcion, String icono) {
        achievementRepository.findByUsuarioAndCodigo(usuario, codigo)
                .orElseGet(() -> achievementRepository.save(new Achievement(usuario, codigo, nombre, descripcion, icono)));
    }
    // METODO para marcar contenido como visto
    @Transactional
    public void markContentAsViewed(Long contenidoId) {
        Usuario usuario = usuarioService.getAuthenticatedUser();
        Contenido contenido = contenidoRepository.findById(contenidoId)
                .orElseThrow(() -> new IllegalArgumentException("Contenido no encontrado"));

        ContenidoRevisadoId id = new ContenidoRevisadoId(usuario.getId(), contenido.getId());

        // Guardamos solo si no existe para evitar errores
        if (!contenidoRevisadoRepository.existsById(id)) {
            ContenidoRevisado revisado = new ContenidoRevisado(usuario, contenido);
            contenidoRevisadoRepository.save(revisado);
        }
    }
    // METODO para obtener los IDs de contenido ya vistos
    @Transactional(readOnly = true)
    public Set<Long> getViewedContentIds(Long moduloId) {
        Usuario usuario = usuarioService.getAuthenticatedUser();
        return contenidoRevisadoRepository.findViewedContentIdsByUsuarioAndModulo(usuario, moduloId);
    }
}
