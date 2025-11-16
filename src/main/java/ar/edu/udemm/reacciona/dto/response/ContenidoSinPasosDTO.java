package ar.edu.udemm.reacciona.dto.response;


public class ContenidoSinPasosDTO {
    private Long id;
    private String titulo;
    private String tipoContenido;
    private String urlRecurso;
    private String cuerpo;
    private Integer orden;

    public ContenidoSinPasosDTO(Long id, String titulo, String tipoContenido, String urlRecurso, String cuerpo, Integer orden) {
        this.id = id;
        this.titulo = titulo;
        this.tipoContenido = tipoContenido;
        this.urlRecurso = urlRecurso;
        this.cuerpo = cuerpo;
        this.orden = orden;
    }

    // Getters y setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getTipoContenido() { return tipoContenido; }
    public void setTipoContenido(String tipoContenido) { this.tipoContenido = tipoContenido; }
    public String getUrlRecurso() { return urlRecurso; }
    public void setUrlRecurso(String urlRecurso) { this.urlRecurso = urlRecurso; }
    public String getCuerpo() { return cuerpo; }
    public void setCuerpo(String cuerpo) { this.cuerpo = cuerpo; }
    public Integer getOrden() { return orden; }
    public void setOrden(Integer orden) { this.orden = orden; }
}