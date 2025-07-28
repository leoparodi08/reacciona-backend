package ar.edu.udemm.reacciona.auth;

import ar.edu.udemm.reacciona.users.Estudiante;
import ar.edu.udemm.reacciona.users.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import ar.edu.udemm.reacciona.config.jwt.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;


@Service
public class AuthService {
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService; // Servicio para crear el token
    private final AuthenticationManager authenticationManager; // Gestor de autenticación


    @Autowired
    public AuthService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder, JwtService jwtService, AuthenticationManager authenticationManager){
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    // metodo para registrar estudiante
    public Estudiante registrarEstudiante(Estudiante estudiante){
        // ciframos la contraseña antes de guardarla
        String hashedPassword = passwordEncoder.encode(estudiante.getPassword());
        estudiante.setPassword(hashedPassword);

        // guardamos el estudiante en la base de datos
        return usuarioRepository.save(estudiante);
    }
    public AuthResponse login(LoginRequest request){
        // 1. Le pedimos a Spring Security que autentique al usuario.
        // Si las credenciales son incorrectas, esto lanzará una excepción automáticamente.
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.email(), request.password()));

        // 2. Si la autenticación fue exitosa, buscamos al usuario para generar el token.
        UserDetails user = usuarioRepository.findByEmail(request.email()).orElseThrow();

        // 3. Generamos el token JWT.
        String token = jwtService.getToken(user);

        // 4. Devolvemos la respuesta con el token.
        return new AuthResponse(token);
    }
}
