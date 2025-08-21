package ar.edu.udemm.reacciona.controller;

import ar.edu.udemm.reacciona.entity.Contenido;
import ar.edu.udemm.reacciona.service.ContenidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contenidos")
public class ContenidoController {

    private final ContenidoService contenidoService;

    @Autowired
    public ContenidoController(ContenidoService contenidoService) {
        this.contenidoService = contenidoService;
    }

    @GetMapping("/modulo/{idModulo}")
    public List<Contenido> obtenerContenidosPorModulo(@PathVariable Long idModulo) {
        return contenidoService.obtenerContenidosPorModulo(idModulo);
    }

    @PostMapping
    public Contenido guardarContenido(@RequestBody Contenido contenido) {
        return contenidoService.guardarContenido(contenido);
    }

    @DeleteMapping("/{id}")
    public void eliminarContenido(@PathVariable Long id) {
        contenidoService.eliminarContenido(id);
    }
}