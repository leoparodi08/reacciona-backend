package ar.edu.udemm.reacciona.modules;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity // 1. Indica que esta clase es una tabla de la base de datos.
public class Modulo {

    @Id // 2. Marca este campo como la clave primaria (ID) de la tabla.
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 3. Le dice a la BD que genere el ID automáticamente.
    private Long id;

    private String titulo;
    private String descripcion;
    private String tipo;

    // JPA necesita un contructor sin argumentos.
    public Modulo() {

    }

    // Contructor para crear nuevos modulos sin un ID (la bd lo genera).
    public Modulo(String titulo, String descripcion, String tipo){
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.tipo = tipo;
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
    public String getTipo() {
        return tipo;
    }
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}
