package ar.edu.udemm.reacciona.auth;

import ar.edu.udemm.reacciona.users.Estudiante;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/auth") // todas las rutas aca empiezan con /api/auth
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService){
        this.authService = authService;
    }

    @PostMapping("/register") // responde a la peticion POST a /api/auth/register
    public Estudiante register(@RequestBody Estudiante estudiante){
        return authService.registrarEstudiante(estudiante);
    }
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request){
        return ResponseEntity.ok(authService.login(request));
    }
}
