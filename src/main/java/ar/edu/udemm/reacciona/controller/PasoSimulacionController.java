package ar.edu.udemm.reacciona.controller;

import ar.edu.udemm.reacciona.entity.PasoSimulacion;
import ar.edu.udemm.reacciona.service.PasoSimulacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pasos-simulacion")
public class PasoSimulacionController {

    @Autowired
    private PasoSimulacionService pasoSimulacionService;

    @GetMapping
    public List<PasoSimulacion> findAll() {
        return pasoSimulacionService.findAll();
    }

    @GetMapping("/{id}")
    public PasoSimulacion findById(@PathVariable Long id) {
        return pasoSimulacionService.findById(id).orElse(null);
    }

    @GetMapping("/contenido/{contenidoId}")
    public List<PasoSimulacion> findByContenidoId(@PathVariable Long contenidoId) {
        return pasoSimulacionService.findByContenidoId(contenidoId);
    }

    @PostMapping
    public PasoSimulacion save(@RequestBody PasoSimulacion pasoSimulacion) {
        return pasoSimulacionService.save(pasoSimulacion);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        pasoSimulacionService.deleteById(id);
    }
}