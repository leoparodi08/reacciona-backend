package ar.edu.udemm.reacciona.modules;

import ar.edu.udemm.reacciona.entity.Clase;
import ar.edu.udemm.reacciona.entity.Contenido;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity // 1. Indica que esta clase es una tabla de la base de datos.
public class Modulo {

    @Id // 2. Marca este campo como la clave primaria (ID) de la tabla.
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 3. Le dice a la BD que genere el ID automáticamente.
    private Long id;

    private String titulo;
    private String descripcion;
    @Enumerated(EnumType.STRING)
    private TipoEmergencia tipoEmergencia;
    @Enumerated(EnumType.STRING)
    private NivelDificultad nivelDificultad;
    private Integer tiempoEstimado;

    @OneToMany(mappedBy = "modulo", cascade = CascadeType.ALL, orphanRemoval = true,  fetch = FetchType.LAZY)
    //@JsonManagedReference
    private List<Contenido> contenidos = new ArrayList<>();


    @ManyToMany(mappedBy = "modulos")
    private List<Clase> clases = new ArrayList<>();

    // JPA necesita un contructor sin argumentos.
    public Modulo() {

    }

    // Contructor para crear nuevos modulos sin un ID (la bd lo genera).
    public Modulo(String titulo, String descripcion, TipoEmergencia tipoEmergencia,NivelDificultad nivelDificultad, Integer tiempoEstimado){
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.tipoEmergencia = tipoEmergencia;
        this.nivelDificultad = nivelDificultad;
        this.tiempoEstimado = tiempoEstimado;
    }

    // Getters y Setters para todos los campos...
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getTitulo() {
        return titulo;
    }
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
    public String getDescripcion() {
        return descripcion;
    }
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    public TipoEmergencia getTipoEmergencia() {
        return tipoEmergencia;
    }
    public void setTipoEmergencia(TipoEmergencia tipoEmergencia) {
        this.tipoEmergencia = tipoEmergencia;
    }
    public NivelDificultad getNivelDificultad() { return nivelDificultad; }
    public void setNivelDificultad(NivelDificultad nivelDificultad) { this.nivelDificultad = nivelDificultad; }
    public Integer getTiempoEstimado() {
        return tiempoEstimado;
    }
    public void setTiempoEstimado(Integer tiempoEstimado) {
        this.tiempoEstimado = tiempoEstimado;
    }
    public List<Contenido> getContenidos() {
        return contenidos;
    }
    public void setContenidos(List<Contenido> contenidos) {
        this.contenidos = contenidos;
    }
}
