package ar.edu.udemm.reacciona;

import ar.edu.udemm.reacciona.modules.Modulo;
import ar.edu.udemm.reacciona.modules.ModuloRepository;
import ar.edu.udemm.reacciona.users.Rol;
import ar.edu.udemm.reacciona.users.RolRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;


@SpringBootApplication
public class ReaccionaApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReaccionaApplication.class, args);
	}

	// Este "Bean" se ejecutará una vez que la aplicación haya iniciado.
	// Lo usaremos para poblar la base de datos con datos de prueba.
	@Bean
	CommandLineRunner commandLineRunner(ModuloRepository moduloRepository) {
		return args -> {
			// primero verificamos si el repositorio de modulos ya tiene registros
			if (moduloRepository.count() == 0){
				System.out.println(">>> Insertando datos de prueba de Módulos...");

				// Si no hay registros, creamos y guardamos los nuevos módulos.
				// Creamos los objetos Modulo usando el constructor sin ID
				Modulo modulo1 = new Modulo("Primeros Auxilios Básicos", "Nociones esenciales para emergencias médicas.", "Médica");
				Modulo modulo2 = new Modulo("Acoso Escolar: ¿Qué hacer?", "Aprende a identificar y actuar ante el bullying.", "Social");
				Modulo modulo3 = new Modulo("Reciclaje y Medio Ambiente", "Cuidar nuestro planeta desde casa.", "Ambiental");

				// Guardamos los módulos en la base de datos usando el repositorio
				moduloRepository.save(modulo1);
				moduloRepository.save(modulo2);
				moduloRepository.save(modulo3);
			} else {
				System.out.println(">>> La tabla de Módulos ya contiene datos. No se insertaron nuevos.");
			}

		};
	}
	@Bean
	CommandLineRunner commandLineRunnerRoles(RolRepository rolRepository) {
		return args -> {
			// Verificamos si la tabla de roles está vacía
			if (rolRepository.count() == 0) {
				System.out.println(">>> Insertando Roles iniciales...");

				Rol rolEstudiante = new Rol();
				rolEstudiante.setNombreRol("Estudiante");

				Rol rolDocente = new Rol();
				rolDocente.setNombreRol("Docente");

				rolRepository.save(rolEstudiante);
				rolRepository.save(rolDocente);
			}
		};
	}
}

