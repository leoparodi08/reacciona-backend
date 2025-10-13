package ar.edu.udemm.reacciona.dashboard;

import ar.edu.udemm.reacciona.progress.ModuleProgress;
import ar.edu.udemm.reacciona.progress.ModuleProgressRepository;
import ar.edu.udemm.reacciona.users.Usuario;
import ar.edu.udemm.reacciona.users.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final UsuarioService usuarioService;
    private final ModuleProgressRepository moduleProgressRepository;

    public DashboardController(UsuarioService usuarioService, ModuleProgressRepository moduleProgressRepository) {
        this.usuarioService = usuarioService;
        this.moduleProgressRepository = moduleProgressRepository;
    }

    @GetMapping("/overview")
    public ResponseEntity<Map<String, Object>> getOverview() {
        Usuario user = usuarioService.getAuthenticatedUser();
        int puntos = user.getPuntos();
        int level = computeLevel(puntos);

        List<ModuleProgress> all = moduleProgressRepository.findByUsuario(user);
        ModuleProgress last = all.stream()
                .max(Comparator.comparing(mp -> mp.getFechaActualizacion() != null ? mp.getFechaActualizacion() : java.time.LocalDateTime.MIN))
                .orElse(null);

        Map<String, Object> body = new HashMap<>();
        body.put("nombre", user.getNombre());
        body.put("puntos", puntos);
        body.put("level", level);
        if (last != null) {
            int porcentaje = last.getPasosTotales() == 0 ? 0 : (int) Math.round((last.getPasosCompletados() * 100.0) / last.getPasosTotales());
            Map<String, Object> lastModule = new HashMap<>();
            lastModule.put("moduloId", last.getModulo().getId());
            lastModule.put("titulo", last.getModulo().getTitulo());
            lastModule.put("porcentaje", porcentaje);
            lastModule.put("pasosCompletados", last.getPasosCompletados());
            lastModule.put("pasosTotales", last.getPasosTotales());
            body.put("lastModule", lastModule);
        }
        return ResponseEntity.ok(body);
    }

    private int computeLevel(int puntos) {
        // Ejemplo simple: cada 500 puntos sube de nivel (nivel mínimo 1)
        return Math.max(1, (puntos / 500) + 1);
    }
}