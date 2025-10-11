package ar.edu.udemm.reacciona.progress;

import ar.edu.udemm.reacciona.entity.PasoSimulacion;
import ar.edu.udemm.reacciona.users.Usuario;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "actividad_intento")
public class ActivityAttempt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_paso", nullable = false)
    private PasoSimulacion pasoSimulacion;

    @Enumerated(EnumType.STRING)
    private ActivityType tipoActividad;

    private boolean correcto;
    private int puntajeOtorgado;

    // Id de la opción elegida (para poder rehidratar la UI). No FK para evitar carga adicional.
    private Long opcionSeleccionadaId;

    @CreationTimestamp
    private LocalDateTime fechaIntento;

    public ActivityAttempt() {}

    public ActivityAttempt(Usuario usuario, PasoSimulacion pasoSimulacion, ActivityType tipoActividad, boolean correcto, int puntajeOtorgado, Long opcionSeleccionadaId) {
        this.usuario = usuario;
        this.pasoSimulacion = pasoSimulacion;
        this.tipoActividad = tipoActividad;
        this.correcto = correcto;
        this.puntajeOtorgado = puntajeOtorgado;
        this.opcionSeleccionadaId = opcionSeleccionadaId;
    }

    public Long getId() { return id; }
    public Usuario getUsuario() { return usuario; }
    public PasoSimulacion getPasoSimulacion() { return pasoSimulacion; }
    public ActivityType getTipoActividad() { return tipoActividad; }
    public boolean isCorrecto() { return correcto; }
    public int getPuntajeOtorgado() { return puntajeOtorgado; }
    public LocalDateTime getFechaIntento() { return fechaIntento; }
    public Long getOpcionSeleccionadaId() { return opcionSeleccionadaId; }
}
