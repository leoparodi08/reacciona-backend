package ar.edu.udemm.reacciona.users;

import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE) // Estrategia de Herencia: una sola tabla
@DiscriminatorColumn(name = "user_type") // Columna que diferencia el tipo de usuario

public abstract class Usuario implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    @Column(unique = true) // el email debe ser unico para cada usuario
    private String email;

    private String password;

    // --- MÉTODOS REQUERIDOS POR UserDetails ---
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Aquí se definirían los roles/permisos (ej. "ROLE_ESTUDIANTE", "ROLE_DOCENTE").
        // Por ahora, devolvemos una lista vacía.
        return List.of();
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
        return id;
    }
    public void setId(Long id) {
        this.id = id;
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
}
