package ar.edu.udemm.reacciona.progress;

import ar.edu.udemm.reacciona.users.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AchievementRepository extends JpaRepository<Achievement, Long> {
    List<Achievement> findByUsuario(Usuario usuario);
    Optional<Achievement> findByUsuarioAndCodigo(Usuario usuario, String codigo);
}
