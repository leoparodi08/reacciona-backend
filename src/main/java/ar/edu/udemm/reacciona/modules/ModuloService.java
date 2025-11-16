package ar.edu.udemm.reacciona.modules;

import ar.edu.udemm.reacciona.dto.response.ContenidoSinPasosDTO;
import ar.edu.udemm.reacciona.dto.response.ModuloSinPasosDTO;
import ar.edu.udemm.reacciona.entity.Contenido;
import ar.edu.udemm.reacciona.entity.PasoSimulacion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service // Marca esta clase como un Servicio de Spring
public class ModuloService {

    private final ModuloRepository moduloRepository;

    // Usamos Inyección de Dependencias en el constructor.
    // Spring automáticamente nos pasará una instancia de ModuloRepository.
    @Autowired
    public ModuloService(ModuloRepository moduloRepository){
        this.moduloRepository = moduloRepository;
    }
    // Lógica de negocio para obtener todos los módulos
    public List<ModuloSinPasosDTO> obtenerTodosLosModulos() {
        List<Modulo> modulos = moduloRepository.findAll();
        return modulos.stream().map(modulo -> {
            List<ContenidoSinPasosDTO> contenidosDTO = modulo.getContenidos().stream()
                    .map(contenido -> new ContenidoSinPasosDTO(
                            contenido.getId(),
                            contenido.getTitulo(),
                            contenido.getTipoContenido() != null ? contenido.getTipoContenido().name() : null,
                            contenido.getUrlRecurso(),
                            contenido.getCuerpo(),
                            contenido.getOrden()
                    )).toList();
            return new ModuloSinPasosDTO(
                    modulo.getId(),
                    modulo.getTitulo(),
                    modulo.getDescripcion(),
                    modulo.getTipoEmergencia() != null ? modulo.getTipoEmergencia().name() : null,
                    modulo.getNivelDificultad() != null ? modulo.getNivelDificultad().name() : null,
                    modulo.getTiempoEstimado(),
                    contenidosDTO
            );
        }).toList();
    }

    public Optional<Modulo> obtenerModuloPorId(Long idModulo) {
        Optional<Modulo> modulo = moduloRepository.findById(idModulo);

        modulo.ifPresent(m -> {
            // Ordena los contenidos por el campo 'orden'
            m.getContenidos().sort(Comparator.comparing(Contenido::getOrden));

            // Inicializa y ordena los pasosSimulacion de cada contenido
            m.getContenidos().forEach(contenido -> {
                contenido.getPasosSimulacion().sort(Comparator.comparing(PasoSimulacion::getOrden));
            });
        });

        return modulo;
    }
}
