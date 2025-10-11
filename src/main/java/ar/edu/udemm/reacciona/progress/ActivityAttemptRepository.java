package ar.edu.udemm.reacciona.progress;

import ar.edu.udemm.reacciona.users.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface ActivityAttemptRepository extends JpaRepository<ActivityAttempt, Long> {
    @Query("select sum(a.puntajeOtorgado) from ActivityAttempt a where a.usuario = :usuario")
    Integer sumPuntajeByUsuario(Usuario usuario);

    List<ActivityAttempt> findByUsuarioAndFechaIntentoBetween(Usuario usuario, LocalDateTime from, LocalDateTime to);

    ActivityAttempt findTop1ByUsuarioAndPasoSimulacion_Contenido_IdOrderByFechaIntentoDesc(Usuario usuario, Long contenidoId);

    long deleteByUsuarioAndPasoSimulacion_Contenido_Id(Usuario usuario, Long contenidoId);
}
