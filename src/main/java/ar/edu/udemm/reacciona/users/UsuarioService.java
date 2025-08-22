package ar.edu.udemm.reacciona.users;

import ar.edu.udemm.reacciona.modules.ModuloRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder){
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Usuario getAuthenticatedUser() {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        return usuarioRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    public Usuario updateUserProfile(UpdateProfileRequest request) {
        Usuario usuario = getAuthenticatedUser();
        usuario.setNombre(request.nombre());
        usuario.setEmail(request.email());
        // Lógica para cambiar contraseña si se proporciona
        return usuarioRepository.save(usuario);
    }
    public void changePassword(ChangePasswordRequest request) {
        Usuario usuario = getAuthenticatedUser();

        if (!passwordEncoder.matches(request.currentPassword(), usuario.getPassword())) {
            throw new IllegalStateException("La contraseña actual es incorrecta.");
        }

        if (!request.newPassword().equals(request.confirmationPassword())) {
            throw new IllegalStateException("La nueva contraseña y la confirmación no coinciden.");
        }

        usuario.setPassword(passwordEncoder.encode(request.newPassword()));
        usuarioRepository.save(usuario);
    }
    public UserProfileDto getAuthenticatedUserProfile() {
        Usuario usuario = getAuthenticatedUser();
        return new UserProfileDto(usuario.getNombre(), usuario.getEmail(), usuario.getPuntos());
    }
}

record UpdateProfileRequest(String nombre, String email) {}