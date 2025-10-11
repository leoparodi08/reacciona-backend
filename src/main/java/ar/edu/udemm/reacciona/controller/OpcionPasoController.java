package ar.edu.udemm.reacciona.controller;

import ar.edu.udemm.reacciona.entity.OpcionPaso;
import ar.edu.udemm.reacciona.service.OpcionPasoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/opciones-paso")
public class OpcionPasoController {

    @Autowired
    private OpcionPasoService opcionPasoService;

    @GetMapping
    public List<OpcionPaso> findAll() {
        return opcionPasoService.findAll();
    }

//    @GetMapping("/{id}")
//    public OpcionPaso findById(@PathVariable Long id) {
//        return opcionPasoService.findById(id).orElse(null);
//    }

//    @GetMapping("/paso/{pasoSimulacionId}")
//    public List<OpcionPaso> findByPasoSimulacionId(@PathVariable Long pasoSimulacionId) {
//        return opcionPasoService.findByPasoSimulacionId(pasoSimulacionId);
//    }

    @PostMapping
    public OpcionPaso save(@RequestBody OpcionPaso opcionPaso) {
        return opcionPasoService.save(opcionPaso);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        opcionPasoService.deleteById(id);
    }
}