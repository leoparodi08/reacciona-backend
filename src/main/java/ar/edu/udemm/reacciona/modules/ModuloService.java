package ar.edu.udemm.reacciona.modules;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
        return modulos;    }
}
