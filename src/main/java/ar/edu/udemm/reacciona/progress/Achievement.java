package ar.edu.udemm.reacciona.progress;

import ar.edu.udemm.reacciona.users.Usuario;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "logro", uniqueConstraints = @UniqueConstraint(columnNames = {"id_usuario", "codigo"}))
public class Achievement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    // Código único interno (ej: FIRST_STEP, PERFECT_MODULE)
    @Column(nullable = false)
    private String codigo;

    private String nombre;
    private String descripcion;
    private String icono; // url o nombre de recurso

    @CreationTimestamp
    private LocalDateTime fechaObtencion;

    public Achievement() {}

    public Achievement(Usuario usuario, String codigo, String nombre, String descripcion, String icono) {
        this.usuario = usuario;
        this.codigo = codigo;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.icono = icono;
    }

    public Long getId() { return id; }
    public Usuario getUsuario() { return usuario; }
    public String getCodigo() { return codigo; }
    public String getNombre() { return nombre; }
    public String getDescripcion() { return descripcion; }
    public String getIcono() { return icono; }
    public LocalDateTime getFechaObtencion() { return fechaObtencion; }
}
