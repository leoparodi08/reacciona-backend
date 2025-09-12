package ar.edu.udemm.reacciona.service;

import ar.edu.udemm.reacciona.entity.Contenido;
import ar.edu.udemm.reacciona.entity.PasoSimulacion;
import ar.edu.udemm.reacciona.repository.ContenidoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class ContenidoService {

    private final ContenidoRepository contenidoRepository;

    @Autowired
    public ContenidoService(ContenidoRepository contenidoRepository) {
        this.contenidoRepository = contenidoRepository;
    }

    public List<Contenido> obtenerContenidos() {
        return contenidoRepository.findAll(); // Filtrar por idModulo si es necesario
    }

    public Contenido obtenerContenido(Long id) {
        Contenido contenido = contenidoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Contenido no encontrado con id: " + id));

        // Ordenar los pasosSimulacion por el campo 'orden'
        contenido.getPasosSimulacion().sort(Comparator.comparing(PasoSimulacion::getOrden));

        return contenido;
    }

    public Contenido guardarContenido(Contenido contenido) {
        return contenidoRepository.save(contenido);
    }

    public void eliminarContenido(Long id) {
        contenidoRepository.deleteById(id);
    }
}