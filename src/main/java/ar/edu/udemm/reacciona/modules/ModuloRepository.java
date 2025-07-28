package ar.edu.udemm.reacciona.modules;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository // Indica que esta es una interfaz de Repositorio de Spring
public interface ModuloRepository extends JpaRepository<Modulo, Long> {
    // ¡Y ya está!
    // JpaRepository nos da métodos como findAll(), findById(), save(), delete()...
}