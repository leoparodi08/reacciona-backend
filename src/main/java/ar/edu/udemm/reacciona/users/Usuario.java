package ar.edu.udemm.reacciona.users;

import ar.edu.udemm.reacciona.entity.Clase;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "usuarios")
public class Usuario implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Long idUsuario;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(name = "password_hash", nullable = false)
    @JsonIgnore
    private String password;

    @CreationTimestamp
    @Column(name = "fecha_creacion", updatable = false)
    private LocalDateTime fechaCreacion;

    @UpdateTimestamp
    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;

    public Rol getRol() {
        return rol;
    }

    // Relación con Roles
    @ManyToOne
    @JoinColumn(name = "id_rol")
    private Rol rol;

    private int puntos = 0; // Por defecto, un nuevo usuario tiene 0 puntos.

    private String resetPasswordToken;
    private LocalDateTime resetPasswordTokenExpiryDate;

    @ManyToOne
    @JoinColumn(name = "id_clase")
    @JsonBackReference
    private Clase clase;

    public Usuario() {
    }
    // --- MÉTODOS REQUERIDOS POR UserDetails ---
    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (rol == null) {
            return List.of();
        }
        return List.of(new SimpleGrantedAuthority(rol.getNombreRol()));
    }
    @Override
    public String getUsername() {
        // Spring Security usará el email como el "username" único.
        return this.email;
    }
    @Override
    public String getPassword() {
        // Devuelve la contraseña cifrada.
        return this.password;
    }
    // Los siguientes métodos los dejamos en "true" para indicar que la cuenta está siempre activa.
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
    @Override
    public boolean isEnabled() {
        return true;
    }

    // Getters y Setters
    public Long getId() {
        return idUsuario;
    }
    public void setId(Long id) {
        this.idUsuario = id;
    }
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getResetPasswordToken() { return resetPasswordToken; }
    public void setResetPasswordToken(String resetPasswordToken) { this.resetPasswordToken = resetPasswordToken; }
    public LocalDateTime getResetPasswordTokenExpiryDate() { return resetPasswordTokenExpiryDate; }
    public void setResetPasswordTokenExpiryDate(LocalDateTime resetPasswordTokenExpiryDate) { this.resetPasswordTokenExpiryDate = resetPasswordTokenExpiryDate; }
    public void setRol(Rol rol) {this.rol = rol;}
    public int getPuntos() { return puntos; }
    public void setPuntos(int puntos) { this.puntos = puntos; }
    public Clase getClase() {
        return clase;
    }

    public void setClase(Clase clase) {
        this.clase = clase;
    }
}
