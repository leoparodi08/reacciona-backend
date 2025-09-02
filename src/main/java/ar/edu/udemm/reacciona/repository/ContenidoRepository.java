package ar.edu.udemm.reacciona.repository;

import ar.edu.udemm.reacciona.entity.Contenido;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ContenidoRepository extends JpaRepository<Contenido, Long> {
}