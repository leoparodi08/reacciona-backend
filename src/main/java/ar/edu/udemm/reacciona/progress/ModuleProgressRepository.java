package ar.edu.udemm.reacciona.progress;

import ar.edu.udemm.reacciona.modules.Modulo;
import ar.edu.udemm.reacciona.users.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ModuleProgressRepository extends JpaRepository<ModuleProgress, Long> {
    Optional<ModuleProgress> findByUsuarioAndModulo(Usuario usuario, Modulo modulo);

    List<ModuleProgress> findByUsuario(Usuario usuario);

    @Query("select mp from ModuleProgress mp where mp.usuario = :usuario " +
        "and (coalesce(:from, mp.fechaActualizacion) = mp.fechaActualizacion or mp.fechaActualizacion >= :from) " +
        "and (coalesce(:to, mp.fechaActualizacion) = mp.fechaActualizacion or mp.fechaActualizacion <= :to)")
    List<ModuleProgress> findByUsuarioAndDateRange(Usuario usuario, LocalDateTime from, LocalDateTime to);
}
