package ar.edu.udemm.reacciona.service;

import ar.edu.udemm.reacciona.entity.OpcionPaso;
import ar.edu.udemm.reacciona.repository.OpcionPasoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OpcionPasoService {

    @Autowired
    private OpcionPasoRepository opcionPasoRepository;

//    public List<OpcionPaso> findByPasoSimulacionId(Long pasoSimulacionId) {
//        return opcionPasoRepository.findByPasoSimulacion(pasoSimulacionId);
//    }

    public List<OpcionPaso> findAll() {
        return opcionPasoRepository.findAll();
    }

    public Optional<OpcionPaso> findById(Long id) {
        return opcionPasoRepository.findById(id);
    }

    public OpcionPaso save(OpcionPaso opcionPaso) {
        return opcionPasoRepository.save(opcionPaso);
    }

    public void deleteById(Long id) {
        opcionPasoRepository.deleteById(id);
    }
}