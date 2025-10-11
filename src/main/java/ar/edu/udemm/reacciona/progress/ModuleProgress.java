package ar.edu.udemm.reacciona.progress;

import ar.edu.udemm.reacciona.modules.Modulo;
import ar.edu.udemm.reacciona.users.Usuario;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "progreso_modulo", uniqueConstraints = @UniqueConstraint(columnNames = {"id_usuario", "id_modulo"}))
public class ModuleProgress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_modulo", nullable = false)
    private Modulo modulo;

    @Enumerated(EnumType.STRING)
    private ModuleProgressStatus status = ModuleProgressStatus.NOT_STARTED;

    private int puntajeTotal = 0;
    private int pasosCompletados = 0;
    private int pasosTotales = 0; // Se setea al crear a partir de contenidos/pasos

    @CreationTimestamp
    private LocalDateTime fechaCreacion;
    @UpdateTimestamp
    private LocalDateTime fechaActualizacion;

    private LocalDateTime fechaComplecion; // cuando se completa

    public ModuleProgress() {}

    public ModuleProgress(Usuario usuario, Modulo modulo, int pasosTotales) {
        this.usuario = usuario;
        this.modulo = modulo;
        this.pasosTotales = pasosTotales;
    }

    public Long getId() { return id; }
    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
    public Modulo getModulo() { return modulo; }
    public void setModulo(Modulo modulo) { this.modulo = modulo; }
    public ModuleProgressStatus getStatus() { return status; }
    public void setStatus(ModuleProgressStatus status) { this.status = status; }
    public int getPuntajeTotal() { return puntajeTotal; }
    public void setPuntajeTotal(int puntajeTotal) { this.puntajeTotal = puntajeTotal; }
    public int getPasosCompletados() { return pasosCompletados; }
    public void setPasosCompletados(int pasosCompletados) { this.pasosCompletados = pasosCompletados; }
    public int getPasosTotales() { return pasosTotales; }
    public void setPasosTotales(int pasosTotales) { this.pasosTotales = pasosTotales; }
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public LocalDateTime getFechaActualizacion() { return fechaActualizacion; }
    public LocalDateTime getFechaComplecion() { return fechaComplecion; }
    public void setFechaComplecion(LocalDateTime fechaComplecion) { this.fechaComplecion = fechaComplecion; }
}
