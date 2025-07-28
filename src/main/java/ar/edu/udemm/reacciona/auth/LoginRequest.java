package ar.edu.udemm.reacciona.auth;

// Un objeto para encapsular los datos de la petición de login
public record LoginRequest(String email, String password) {
}