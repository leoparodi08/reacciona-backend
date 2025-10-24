package ar.edu.udemm.reacciona.users;

import ar.edu.udemm.reacciona.entity.Clase;

public record UserProfileDto(Long idUsuario, String nombre, String email, int puntos, Integer idRol, Long idClase) {
}