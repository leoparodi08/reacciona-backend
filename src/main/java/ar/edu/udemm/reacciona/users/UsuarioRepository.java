package ar.edu.udemm.reacciona.users;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    // Spring Data JPA nos dará los métodos básicos como save(), findById(), etc.
    // Spring Data JPA creará la implementación de este metodo automáticamente
    Optional<Usuario> findByEmail(String email);
    Optional<Usuario> findByResetPasswordToken(String token);
    List<Usuario> findByRolIdRolAndClaseIsNull(Integer idRol);
}