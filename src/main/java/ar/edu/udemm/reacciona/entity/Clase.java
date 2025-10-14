package ar.edu.udemm.reacciona.entity;

import ar.edu.udemm.reacciona.modules.Modulo;
import ar.edu.udemm.reacciona.users.Usuario;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Clase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombreClase;

    private String descripcion;

    @OneToMany(mappedBy = "clase", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Usuario> alumnos = new ArrayList<>();

    private Long idDocenteCreador;

    @ManyToMany
    @JoinTable(
            name = "clase_modulo",
            joinColumns = @JoinColumn(name = "clase_id"),
            inverseJoinColumns = @JoinColumn(name = "modulo_id")
    )
    private List<Modulo> modulos = new ArrayList<>();

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public List<Usuario> getAlumnos() {
        return alumnos;
    }

    public void setAlumnos(List<Usuario> alumnos) {
        this.alumnos = alumnos;
    }

    public Long getIdDocenteCreador() {
        return idDocenteCreador;
    }

    public void setIdDocenteCreador(Long idDocenteCreador) {
        this.idDocenteCreador = idDocenteCreador;
    }

    public List<Modulo> getModulos() {
        return modulos;
    }

    public void setModulos(List<Modulo> modulos) {
        this.modulos = modulos;
    }
}