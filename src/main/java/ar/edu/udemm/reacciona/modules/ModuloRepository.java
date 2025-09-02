package ar.edu.udemm.reacciona.modules;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository // Indica que esta es una interfaz de Repositorio de Spring
public interface ModuloRepository extends JpaRepository<Modulo, Long> {
    // ¡Y ya está!
    @EntityGraph(attributePaths = "contenidos")
    List<Modulo> findAll();// JpaRepository nos da métodos como findAll(), findById(), save(), delete()...
}