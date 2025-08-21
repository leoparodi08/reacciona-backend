package ar.edu.udemm.reacciona.service;

import ar.edu.udemm.reacciona.entity.Contenido;
import ar.edu.udemm.reacciona.repository.ContenidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContenidoService {

    private final ContenidoRepository contenidoRepository;

    @Autowired
    public ContenidoService(ContenidoRepository contenidoRepository) {
        this.contenidoRepository = contenidoRepository;
    }

    public List<Contenido> obtenerContenidosPorModulo(Long idModulo) {
        return contenidoRepository.findAll(); // Filtrar por idModulo si es necesario
    }

    public Contenido guardarContenido(Contenido contenido) {
        return contenidoRepository.save(contenido);
    }

    public void eliminarContenido(Long id) {
        contenidoRepository.deleteById(id);
    }
}