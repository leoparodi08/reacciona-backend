package ar.edu.udemm.reacciona.repository;

import ar.edu.udemm.reacciona.entity.Clase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ClaseRepository extends JpaRepository<Clase, Long> {
    
    /**
     * Encuentra todas las clases donde el docente es el creador
     */
    List<Clase> findByIdDocenteCreador(Long idDocente);
    
    /**
     * Encuentra una clase específica del docente
     */
    @Query("SELECT c FROM Clase c WHERE c.id = :claseId AND c.idDocenteCreador = :docenteId")
    Clase findByIdAndDocenteCreador(@Param("claseId") Long claseId, @Param("docenteId") Long docenteId);
    
    /**
     * Verifica si una clase pertenece a un docente específico
     */
    @Query("SELECT COUNT(c) > 0 FROM Clase c WHERE c.id = :claseId AND c.idDocenteCreador = :docenteId")
    boolean existsByIdAndDocenteCreador(@Param("claseId") Long claseId, @Param("docenteId") Long docenteId);
}