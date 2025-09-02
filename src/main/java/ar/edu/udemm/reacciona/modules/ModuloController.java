package ar.edu.udemm.reacciona.modules;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController // 1. Anotación que le dice a Spring que esta clase es un controlador REST
@RequestMapping("/api/modulos") // 2. Todas las rutas en esta clase empezarán con /api/modulos
public class ModuloController {

    private ModuloService moduloService;
    //Inyectamos el servicio en el Controlador
    @Autowired
    public ModuloController(ModuloService moduloService){
        this.moduloService = moduloService;
    }

    // 3. Este metodo manejará las peticiones GET a /api/modulos
    @GetMapping
    public List<Modulo> obtenerTodosLosModulos() {
        // El controlador ahora solo delega la llamada al servicio. ¡Está mucho más limpio!
        return moduloService.obtenerTodosLosModulos();
    }
    @GetMapping("/test-protegido")
    public String getTestProtegido() {
        return "¡Si ves esto, estás autenticado!";
    }

    @GetMapping("/{idModulo}")
    public ResponseEntity<Modulo> obtenerModuloPorId(@PathVariable Long idModulo) {
        Optional<Modulo> modulo = moduloService.obtenerModuloPorId(idModulo);
        return modulo.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

}
