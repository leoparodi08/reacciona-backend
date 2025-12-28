package ar.edu.udemm.reacciona.dto.response;

import java.util.List;

public class ClaseSinContenidosDTO {
    private Long id;
    private String nombreClase;
    private String descripcion;
    private String nombreDocente;
    private List<UsuarioSinRolDTO> alumnos;
    private List<ModuloSinContenidosDTO> modulos;

    public ClaseSinContenidosDTO(Long id, String nombreClase, String descripcion, String nombreDocente, List<UsuarioSinRolDTO> alumnos, List<ModuloSinContenidosDTO> modulos) {
        this.id = id;
        this.nombreClase = nombreClase;
        this.descripcion = descripcion;
        this.nombreDocente = nombreDocente;
        this.alumnos = alumnos;
        this.modulos = modulos;
    }

    // Getters y setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombreClase() { return nombreClase; }
    public void setNombreClase(String nombreClase) { this.nombreClase = nombreClase; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public String getNombreDocente() { return nombreDocente; }
    public void setNombreDocente(String nombreDocente) { this.nombreDocente = nombreDocente; }
    public List<UsuarioSinRolDTO> getAlumnos() { return alumnos; }
    public void setAlumnos(List<UsuarioSinRolDTO> alumnos) { this.alumnos = alumnos; }
    public List<ModuloSinContenidosDTO> getModulos() { return modulos; }
    public void setModulos(List<ModuloSinContenidosDTO> modulos) { this.modulos = modulos; }
}