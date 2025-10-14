package ar.edu.udemm.reacciona.users;

public record UserProfileDto(Long idUsuario, String nombre, String email, int puntos, Integer idRol) {
}