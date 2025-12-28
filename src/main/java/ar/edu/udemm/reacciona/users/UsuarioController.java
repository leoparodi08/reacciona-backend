package ar.edu.udemm.reacciona.users;


import ar.edu.udemm.reacciona.dto.request.UpdateUsuarioRolRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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

    @GetMapping("/all/{idUsuario}") // Endpoint para obtener todos los perfiles de usuario
    public ResponseEntity<List<UserProfileDto>> getAllProfile(@PathVariable Long idUsuario) {
        return ResponseEntity.ok(usuarioService.getAllUserProfiles(idUsuario));
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

    @PutMapping("/update-role")
    public ResponseEntity<Usuario> updateUserRole(@RequestBody UpdateUserRolRequest request) {
        Usuario updatedUser = usuarioService.updateUserRole(request.getIdUsuario(), request.getIdRol());
        return ResponseEntity.ok(updatedUser);
    }

    @GetMapping("/rol/docentes")
    public ResponseEntity<List<Usuario>> getUsuariosWithRolId2() {
        List<Usuario> usuarios = usuarioService.getUsuariosWithRolId2();
        return ResponseEntity.ok(usuarios);
    }

    @GetMapping("/rol/estudiante/clase-empty")
    public ResponseEntity<List<Usuario>> getUsuariosWithRolId1AndClaseNull() {
        List<Usuario> usuarios = usuarioService.getUsuariosWithRolId1AndClaseNull();
        return ResponseEntity.ok(usuarios);
    }

    @PostMapping("/{idClase}/asignar-clase")
    public ResponseEntity<Void> assignClaseToUsuarios(
            @PathVariable Long idClase,
            @RequestBody List<Long> alumnosIds) {
        usuarioService.updateUsuariosClase(idClase, alumnosIds);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/update-roles")
    public ResponseEntity<Void> updateUsuariosRoles(@RequestBody List<UpdateUsuarioRolRequest> usuariosRoles) {
        usuarioService.updateUsuariosRoles(usuariosRoles);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{idUsuario}/remove-clase")
    public ResponseEntity<Void> removeClaseFromUsuario(@PathVariable Long idUsuario) {
        usuarioService.removeClaseFromUsuario(idUsuario);
        return ResponseEntity.ok().build();
    }

}