package ar.edu.udemm.reacciona.modules;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public List<Modulo> obtenerTodosLosModulos() {
        List<Modulo> modulos = moduloRepository.findAll();
        // Inicializar la colección de contenidos para evitar problemas de LazyInitializationException
        modulos.forEach(modulo -> modulo.getContenidos().size());
        return modulos;
    }

    public Optional<Modulo> obtenerModuloPorId(Long idModulo) {
        Optional<Modulo> modulo = moduloRepository.findById(idModulo);
        modulo.ifPresent(m -> m.getContenidos().size()); // Inicializa la colección
        return modulo;
    }
}
