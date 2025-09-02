package ar.edu.udemm.reacciona.users;

public record ChangePasswordRequest(String currentPassword, String newPassword, String confirmationPassword) {
}