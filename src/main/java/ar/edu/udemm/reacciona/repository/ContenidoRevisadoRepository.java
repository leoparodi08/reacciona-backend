package ar.edu.udemm.reacciona.repository;

import ar.edu.udemm.reacciona.entity.ContenidoRevisado;
import ar.edu.udemm.reacciona.entity.ContenidoRevisadoId;
import ar.edu.udemm.reacciona.users.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface ContenidoRevisadoRepository extends JpaRepository<ContenidoRevisado, ContenidoRevisadoId> {

    // Query para obtener los IDs de contenido vistos por un usuario para un módulo específico
    @Query("SELECT cv.contenido.id FROM ContenidoRevisado cv WHERE cv.usuario = :usuario AND cv.contenido.modulo.id = :idModulo")
    Set<Long> findViewedContentIdsByUsuarioAndModulo(Usuario usuario, Long idModulo);
}