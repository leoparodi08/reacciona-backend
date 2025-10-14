package ar.edu.udemm.reacciona.controller;

import ar.edu.udemm.reacciona.dto.request.CreateClaseRequest;
import ar.edu.udemm.reacciona.entity.Clase;
import ar.edu.udemm.reacciona.service.ClaseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clases")
public class ClaseController {

    private final ClaseService claseService;

    public ClaseController(ClaseService claseService) {
        this.claseService = claseService;
    }

    @PostMapping
    public ResponseEntity<Clase> createClase(@RequestBody CreateClaseRequest request) {
        Clase clase = claseService.createClase(
                request.getNombreClase(),
                request.getDescripcion(),
                request.getIdDocenteCreador()
        );
        return ResponseEntity.ok(clase);
    }

    @PostMapping("/{claseId}/modulos")
    public ResponseEntity<Clase> associateModulosWithClase(
            @PathVariable Long claseId,
            @RequestBody List<Long> moduloIds) {
        Clase updatedClase = claseService.associateModulosWithClase(claseId, moduloIds);
        return ResponseEntity.ok(updatedClase);
    }

}