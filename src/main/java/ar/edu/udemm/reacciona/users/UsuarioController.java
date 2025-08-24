package ar.edu.udemm.reacciona.users;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
// ...

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {
    private final UsuarioService usuarioService;

    @Autowired
    public UsuarioController(UsuarioService usuarioService){
        this.usuarioService = usuarioService;
    }

    @GetMapping("/me") // Endpoint para obtener datos del usuario actual
    public ResponseEntity<UserProfileDto> getMyProfile() {
        return ResponseEntity.ok(usuarioService.getAuthenticatedUserProfile());
    }

    @PutMapping("/me") // Endpoint para actualizar el perfil
    public ResponseEntity<Usuario> updateMyProfile(@RequestBody UpdateProfileRequest request) {
        return ResponseEntity.ok(usuarioService.updateUserProfile(request));
    }
    @PutMapping("/me/change-password")
    public ResponseEntity<?> changeMyPassword(@RequestBody ChangePasswordRequest request) {
        try {
            usuarioService.changePassword(request);
            return ResponseEntity.ok("Contraseña actualizada exitosamente.");
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}