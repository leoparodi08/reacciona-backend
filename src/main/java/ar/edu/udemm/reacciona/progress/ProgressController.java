package ar.edu.udemm.reacciona.progress;

import ar.edu.udemm.reacciona.progress.dto.ProgressSummaryDto;
import ar.edu.udemm.reacciona.entity.PasoSimulacion;
import ar.edu.udemm.reacciona.repository.PasoSimulacionRepository;
import org.springframework.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.security.Principal;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Set;

@RestController
@RequestMapping("/api/progress")
public class ProgressController {

    private final ProgressService progressService;
    private final PasoSimulacionRepository pasoSimulacionRepository;
    private static final Logger log = LoggerFactory.getLogger(ProgressController.class);

    public ProgressController(ProgressService progressService, PasoSimulacionRepository pasoSimulacionRepository) {
        this.progressService = progressService;
        this.pasoSimulacionRepository = pasoSimulacionRepository;
    }

    @GetMapping("/summary")
    public ResponseEntity<ProgressSummaryDto> getSummary(@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
                                                         @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
                                                         @RequestParam(required = false, name = "tipoEmergencia") String tipoEmergencia,
                                                         @RequestParam(required = false, name = "moduleId") Long moduleId) {
        log.info("[GET /api/progress/summary] from={}, to={}, tipoEmergencia={}, moduleId={}", from, to, tipoEmergencia, moduleId);
        ProgressSummaryDto dto = progressService.getSummary(from, to, tipoEmergencia, moduleId);
        log.info("Summary => modules={}, completed={}, score={}", dto.totalModules(), dto.completedModules(), dto.totalScore());
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/ping")
    public ResponseEntity<String> ping(Principal principal) {
        return ResponseEntity.ok("OK user=" + (principal != null ? principal.getName() : "null"));
    }

    @PostMapping("/attempt")
    public ResponseEntity<?> registerAttempt(@RequestBody AttemptRequest request, Principal principal) {
        log.info("[POST /api/progress/attempt] user={}, pasoId={}, correcto={}, puntaje={}, opcionSeleccionada={}",
                principal != null ? principal.getName() : "?", request.pasoId(), request.correcto(), request.puntaje(), request.opcionSeleccionadaId());
        PasoSimulacion paso = pasoSimulacionRepository.findById(request.pasoId())
                .orElseThrow(() -> new IllegalArgumentException("Paso no encontrado"));
        progressService.registerAttempt(paso, request.correcto(), request.puntaje(), request.opcionSeleccionadaId());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/simulation/state")
    public ResponseEntity<?> getSimulationState(@RequestParam Long contenidoId) {
        return ResponseEntity.ok(progressService.getSimulationState(contenidoId));
    }

    @PostMapping("/simulation/reset")
    public ResponseEntity<?> resetSimulation(@RequestParam Long contenidoId) {
        progressService.resetSimulation(contenidoId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/attempts")
    public ResponseEntity<?> listAttempts(Principal principal) {
        // Simple debug endpoint: retorna conteo de intentos por usuario (no expone todos los datos para mantenerlo corto)
        log.info("[GET /api/progress/attempts] user={}", principal != null ? principal.getName() : "?");
        return ResponseEntity.ok("Endpoint de depuracion: revisar tabla actividad_intento directamente para detalle.");
    }

    // ENDPOINT para marcar un contenido como visto
    @PostMapping("/content/{contenidoId}/mark-as-viewed")
    public ResponseEntity<?> markContentAsViewed(@PathVariable Long contenidoId) {
        progressService.markContentAsViewed(contenidoId);
        return ResponseEntity.ok().build();
    }

    // ENDPOINT para obtener los IDs de contenido vistos de un módulo
    @GetMapping("/module/{moduloId}/viewed-content")
    public ResponseEntity<Set<Long>> getViewedContentIds(@PathVariable Long moduloId) {
        return ResponseEntity.ok(progressService.getViewedContentIds(moduloId));
    }
}

record AttemptRequest(Long pasoId, boolean correcto, int puntaje, Long opcionSeleccionadaId) {}
