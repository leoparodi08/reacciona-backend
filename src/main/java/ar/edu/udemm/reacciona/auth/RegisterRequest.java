package ar.edu.udemm.reacciona.auth;

// Este objeto representa los datos que el frontend envía para registrar un usuario.
public record RegisterRequest(String nombre, String email, String password) {
}