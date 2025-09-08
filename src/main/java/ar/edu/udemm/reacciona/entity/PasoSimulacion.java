package ar.edu.udemm.reacciona.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
@Entity
public class PasoSimulacion {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPaso;

    @ManyToOne
    @JoinColumn(name = "id_contenido", nullable = false)
    @JsonBackReference
    private Contenido contenido;

    private String descripcion;

    private Integer orden;

    private String escenario;

    @OneToMany(mappedBy = "pasoSimulacion", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<OpcionPaso> opcionesPaso = new ArrayList<>();

    public PasoSimulacion() {
    }

    public PasoSimulacion(Long idPaso, Contenido contenido, String descripcion, Integer orden, String escenario, List<OpcionPaso> opcionesPaso) {
        this.idPaso = idPaso;
        this.contenido = contenido;
        this.descripcion = descripcion;
        this.orden = orden;
        this.escenario = escenario;
        this.opcionesPaso = opcionesPaso;
    }

    public Long getIdPaso() {
        return idPaso;
    }

    public void setIdPaso(Long idPaso) {
        this.idPaso = idPaso;
    }

    public Contenido getContenido() {
        return contenido;
    }

    public void setContenido(Contenido contenido) {
        this.contenido = contenido;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Integer getOrden() {
        return orden;
    }

    public void setOrden(Integer orden) {
        this.orden = orden;
    }

    public String getEscenario() {
        return escenario;
    }

    public void setEscenario(String escenario) {
        this.escenario = escenario;
    }

    public List<OpcionPaso> getOpcionesPaso() {
        return opcionesPaso;
    }

    public void setOpcionesPaso(List<OpcionPaso> opcionesPaso) {
        this.opcionesPaso = opcionesPaso;
    }

}
