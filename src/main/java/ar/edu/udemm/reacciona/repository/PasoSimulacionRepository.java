package ar.edu.udemm.reacciona.repository;

import ar.edu.udemm.reacciona.entity.PasoSimulacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PasoSimulacionRepository extends JpaRepository<PasoSimulacion, Long> {
    List<PasoSimulacion> findByContenidoId(Long contenidoId);
}