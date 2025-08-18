package ar.edu.udemm.reacciona.modules;

import jakarta.persistence.*;

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
}
