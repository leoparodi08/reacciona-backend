package ar.edu.udemm.reacciona.dto.response;

import java.util.List;

public class ModuloSinPasosDTO {
    private Long id;
    private String titulo;
    private String descripcion;
    private String tipoEmergencia;
    private String nivelDificultad;
    private Integer tiempoEstimado;
    private List<ContenidoSinPasosDTO> contenidos;

    public ModuloSinPasosDTO(Long id, String titulo, String descripcion, String tipoEmergencia, String nivelDificultad, Integer tiempoEstimado, List<ContenidoSinPasosDTO> contenidos) {
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.tipoEmergencia = tipoEmergencia;
        this.nivelDificultad = nivelDificultad;
        this.tiempoEstimado = tiempoEstimado;
        this.contenidos = contenidos;
    }

    // Getters y setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public String getTipoEmergencia() { return tipoEmergencia; }
    public void setTipoEmergencia(String tipoEmergencia) { this.tipoEmergencia = tipoEmergencia; }
    public String getNivelDificultad() { return nivelDificultad; }
    public void setNivelDificultad(String nivelDificultad) { this.nivelDificultad = nivelDificultad; }
    public Integer getTiempoEstimado() { return tiempoEstimado; }
    public void setTiempoEstimado(Integer tiempoEstimado) { this.tiempoEstimado = tiempoEstimado; }
    public List<ContenidoSinPasosDTO> getContenidos() { return contenidos; }
    public void setContenidos(List<ContenidoSinPasosDTO> contenidos) { this.contenidos = contenidos; }
}