package ar.edu.udemm.reacciona.service;

import ar.edu.udemm.reacciona.entity.Clase;
import ar.edu.udemm.reacciona.modules.Modulo;
import ar.edu.udemm.reacciona.modules.ModuloRepository;
import ar.edu.udemm.reacciona.repository.ClaseRepository;
import ar.edu.udemm.reacciona.users.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ClaseService {

    private final ClaseRepository claseRepository;
    private final UsuarioRepository usuarioRepository;
    private final ModuloRepository moduloRepository;


    public ClaseService(ClaseRepository claseRepository, UsuarioRepository usuarioRepository, ModuloRepository moduloRepository) {
        this.claseRepository = claseRepository;
        this.usuarioRepository = usuarioRepository;
        this.moduloRepository = moduloRepository;
    }

    @Transactional
    public Clase createClase(String nombreClase, String descripcion, Long idDocenteCreador) {
        // Validate docente
        if (!usuarioRepository.existsById(idDocenteCreador)) {
            throw new IllegalArgumentException("Docente no encontrado con id: " + idDocenteCreador);
        }

        // Create and save Clase
        Clase clase = new Clase();
        clase.setNombreClase(nombreClase);
        clase.setDescripcion(descripcion);
        clase.setIdDocenteCreador(idDocenteCreador);

        return claseRepository.save(clase);
    }



    @Transactional
    public Clase associateModulosWithClase(Long claseId, List<Long> moduloIds) {
        // Validate Clase
        Clase clase = claseRepository.findById(claseId)
                .orElseThrow(() -> new IllegalArgumentException("Clase no encontrada con id: " + claseId));

        // Fetch Modulos
        List<Modulo> modulos = moduloRepository.findAllById(moduloIds);

        // Associate Modulos with Clase
        clase.getModulos().addAll(modulos);

        // Save Clase
        return claseRepository.save(clase);
    }

    public List<Clase> getAllClases() {
        List<Clase> clases = claseRepository.findAll();
        clases.forEach(clase -> {
            if (clase.getIdDocenteCreador() != null) {
                usuarioRepository.findById(clase.getIdDocenteCreador())
                        .ifPresent(usuario -> clase.setNombreDocente(usuario.getNombre()));
            }
        });
        return clases;
    }

}