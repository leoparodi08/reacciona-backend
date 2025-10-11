package ar.edu.udemm.reacciona.repository;

import ar.edu.udemm.reacciona.entity.OpcionPaso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OpcionPasoRepository extends JpaRepository<OpcionPaso, Long> {
//    List<OpcionPaso> findByPasoSimulacion(Long idPaso);
}