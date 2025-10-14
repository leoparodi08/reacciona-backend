package ar.edu.udemm.reacciona.dto.request;

public class CreateClaseRequest {
    private String nombreClase;
    private String descripcion;
    private Long idDocenteCreador;

    // Getters and Setters
    public String getNombreClase() {
        return nombreClase;
    }

    public void setNombreClase(String nombreClase) {
        this.nombreClase = nombreClase;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Long getIdDocenteCreador() {
        return idDocenteCreador;
    }

    public void setIdDocenteCreador(Long idDocenteCreador) {
        this.idDocenteCreador = idDocenteCreador;
    }
}