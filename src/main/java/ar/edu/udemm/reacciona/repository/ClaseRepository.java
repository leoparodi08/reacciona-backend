package ar.edu.udemm.reacciona.repository;

import ar.edu.udemm.reacciona.entity.Clase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClaseRepository extends JpaRepository<Clase, Long> {
}