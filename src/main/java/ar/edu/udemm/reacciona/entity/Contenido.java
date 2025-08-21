package ar.edu.udemm.reacciona.entity;

import ar.edu.udemm.reacciona.modules.Modulo;
import ar.edu.udemm.reacciona.utils.TipoContenido;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

@Entity
public class Contenido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_modulo", nullable = false)
    @JsonBackReference
    private Modulo modulo;

    private String titulo;

    @Enumerated(EnumType.STRING)
    private TipoContenido tipoContenido;

    private String urlRecurso;

    @Lob
    private String cuerpo;

    private Integer orden;

    // Constructor vacío requerido por JPA
    public Contenido() {}

    // Constructor con parámetros
    public Contenido(Modulo modulo, String titulo, TipoContenido tipoContenido, String urlRecurso, String cuerpo, Integer orden) {
        this.modulo = modulo;
        this.titulo = titulo;
        this.tipoContenido = tipoContenido;
        this.urlRecurso = urlRecurso;
        this.cuerpo = cuerpo;
        this.orden = orden;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Modulo getModulo() {
        return modulo;
    }

    public void setModulo(Modulo modulo) {
        this.modulo = modulo;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public TipoContenido getTipoContenido() {
        return tipoContenido;
    }

    public void setTipoContenido(TipoContenido tipoContenido) {
        this.tipoContenido = tipoContenido;
    }

    public String getUrlRecurso() {
        return urlRecurso;
    }

    public void setUrlRecurso(String urlRecurso) {
        this.urlRecurso = urlRecurso;
    }

    public String getCuerpo() {
        return cuerpo;
    }

    public void setCuerpo(String cuerpo) {
        this.cuerpo = cuerpo;
    }

    public Integer getOrden() {
        return orden;
    }

    public void setOrden(Integer orden) {
        this.orden = orden;
    }
}
