package ar.edu.udemm.reacciona.entity;

import ar.edu.udemm.reacciona.users.Usuario;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "contenido_revisado")
@IdClass(ContenidoRevisadoId.class)
public class ContenidoRevisado {

    @Id
    @ManyToOne
    @JoinColumn(name = "id_usuario", referencedColumnName = "id_usuario")
    private Usuario usuario;

    @Id
    @ManyToOne
    @JoinColumn(name = "id_contenido", referencedColumnName = "id")
    private Contenido contenido;

    @CreationTimestamp
    @Column(name = "fecha_visto", updatable = false)
    private LocalDateTime fechaVisto;

    // Constructores, getters, setters
    public ContenidoRevisado() {}

    public ContenidoRevisado(Usuario usuario, Contenido contenido) {
        this.usuario = usuario;
        this.contenido = contenido;
    }
}