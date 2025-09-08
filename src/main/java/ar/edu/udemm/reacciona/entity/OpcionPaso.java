package ar.edu.udemm.reacciona.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

@Entity
public class OpcionPaso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idOpcion;

    @ManyToOne
    @JoinColumn(name = "id_paso", nullable = false)
    @JsonBackReference
    private PasoSimulacion pasoSimulacion;

    private String textoOpcion;

    private Boolean esCorrecto;

    private String feedback;

    public Long getIdOpcion() {
        return idOpcion;
    }

    public void setIdOpcion(Long idOpcion) {
        this.idOpcion = idOpcion;
    }

    public PasoSimulacion getPasoSimulacion() {
        return pasoSimulacion;
    }

    public void setPasoSimulacion(PasoSimulacion pasoSimulacion) {
        this.pasoSimulacion = pasoSimulacion;
    }

    public String getTextoOpcion() {
        return textoOpcion;
    }

    public void setTextoOpcion(String textoOpcion) {
        this.textoOpcion = textoOpcion;
    }

    public Boolean getEsCorrecto() {
        return esCorrecto;
    }

    public void setEsCorrecto(Boolean esCorrecto) {
        this.esCorrecto = esCorrecto;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

}