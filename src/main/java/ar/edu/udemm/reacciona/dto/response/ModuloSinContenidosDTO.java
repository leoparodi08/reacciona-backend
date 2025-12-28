package ar.edu.udemm.reacciona.dto.response;


public class ModuloSinContenidosDTO {
    private Long id;
    private String titulo;
    private String descripcion;
    private String tipoEmergencia;
    private String nivelDificultad;


    public ModuloSinContenidosDTO(Long id, String titulo, String descripcion, String tipoEmergencia, String nivelDificultad){
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.tipoEmergencia = tipoEmergencia;
        this.nivelDificultad = nivelDificultad;

    }

    // Getters y setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getTipoEmergencia() {
        return tipoEmergencia;
    }

    public void setTipoEmergencia(String tipoEmergencia) {
        this.tipoEmergencia = tipoEmergencia;
    }

    public String getNivelDificultad() {
        return nivelDificultad;
    }

    public void setNivelDificultad(String nivelDificultad) {
        this.nivelDificultad = nivelDificultad;
    }
}
