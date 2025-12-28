package ar.edu.udemm.reacciona.entity;

import java.io.Serializable;
import java.util.Objects;

public class ContenidoRevisadoId implements Serializable{
    private Long usuario;
    private Long contenido;

    public ContenidoRevisadoId() {}

    public ContenidoRevisadoId(Long usuario, Long contenido) {
        this.usuario = usuario;
        this.contenido = contenido;
    }

    // Getters y Setters
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ContenidoRevisadoId that = (ContenidoRevisadoId) o;
        return Objects.equals(usuario, that.usuario) && Objects.equals(contenido, that.contenido);
    }

    @Override
    public int hashCode() {
        return Objects.hash(usuario, contenido);
    }
}
