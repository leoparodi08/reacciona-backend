package ar.edu.udemm.reacciona.service;

import ar.edu.udemm.reacciona.entity.PasoSimulacion;
import ar.edu.udemm.reacciona.repository.PasoSimulacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PasoSimulacionService {

    @Autowired
    private PasoSimulacionRepository pasoSimulacionRepository;

    public List<PasoSimulacion> findByContenidoId(Long contenidoId) {
        return pasoSimulacionRepository.findByContenidoId(contenidoId);
    }

    public List<PasoSimulacion> findAll() {
        return pasoSimulacionRepository.findAll();
    }

    public Optional<PasoSimulacion> findById(Long id) {
        return pasoSimulacionRepository.findById(id);
    }

    public PasoSimulacion save(PasoSimulacion pasoSimulacion) {
        return pasoSimulacionRepository.save(pasoSimulacion);
    }

    public void deleteById(Long id) {
        pasoSimulacionRepository.deleteById(id);
    }
}