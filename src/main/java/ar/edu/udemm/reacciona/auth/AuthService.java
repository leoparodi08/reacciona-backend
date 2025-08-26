package ar.edu.udemm.reacciona.auth;

import java.time.LocalDateTime;
import java.util.UUID;

import ar.edu.udemm.reacciona.users.Usuario;
import ar.edu.udemm.reacciona.users.UsuarioRepository;
import ar.edu.udemm.reacciona.users.Rol;
import ar.edu.udemm.reacciona.users.RolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import ar.edu.udemm.reacciona.config.jwt.JwtService;


@Service
public class AuthService {
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService; // Servicio para crear el token
    private final AuthenticationManager authenticationManager; // Gestor de autenticación
    private final JavaMailSender mailSender;
    private final RolRepository rolRepository;

    @Autowired
    public AuthService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder, JwtService jwtService, AuthenticationManager authenticationManager, JavaMailSender mailSender, RolRepository rolRepository){
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.mailSender= mailSender;
        this.rolRepository= rolRepository;
    }

    public Usuario registrarEstudiante(RegisterRequest request){
        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setNombre(request.nombre());
        nuevoUsuario.setEmail(request.email());
        // ciframos la contraseña antes de guardarla
        String hashedPassword = passwordEncoder.encode(request.password());
        nuevoUsuario.setPassword(hashedPassword);
        // Se asigna el rol de "Estudiante" por defecto
        Rol rolEstudiante = rolRepository.findByNombreRol("Estudiante")
                .orElseThrow(() -> new RuntimeException("Error: Rol no encontrado."));
        nuevoUsuario.setRol(rolEstudiante);

        Usuario usuarioGuardado = usuarioRepository.save(nuevoUsuario);

        sendWelcomeEmail(usuarioGuardado.getEmail(), usuarioGuardado.getNombre());
        return usuarioGuardado;
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
    public void processForgotPassword(String email) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElse(null); // No lanzamos error para no revelar si un email existe

        if (usuario != null) {
            String token = UUID.randomUUID().toString();
            usuario.setResetPasswordToken(token);
            // El token expira en 1 hora
            usuario.setResetPasswordTokenExpiryDate(LocalDateTime.now().plusHours(1));
            usuarioRepository.save(usuario);

            String resetLink = "http://localhost:3000/reset-password?token=" + token;
            sendPasswordResetEmail(email, resetLink);
        }
    }

    public void resetPassword(String token, String newPassword) {
        Usuario usuario = usuarioRepository.findByResetPasswordToken(token)
                .orElseThrow(() -> new RuntimeException("Token inválido o expirado."));

        if (usuario.getResetPasswordTokenExpiryDate().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("El token ha expirado.");
        }

        usuario.setPassword(passwordEncoder.encode(newPassword));
        usuario.setResetPasswordToken(null);
        usuario.setResetPasswordTokenExpiryDate(null);
        usuarioRepository.save(usuario);
    }

    private void sendPasswordResetEmail(String to, String link) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Reacciona - Restablecer tu Contraseña");
        message.setText("Hola,\n\nPara restablecer tu contraseña, haz clic en el siguiente enlace:\n" + link
                + "\n\nSi no solicitaste esto, por favor ignora este correo.\n\nSaludos,\nEl equipo de Reacciona");
        mailSender.send(message);
    }
    private void sendWelcomeEmail(String to, String name) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("¡Bienvenido/a a Reacciona!");
        message.setText("Hola, " + name + ".\n\n"
                + "Tu cuenta en la plataforma Reacciona ha sido creada exitosamente.\n\n"
                + "Ya puedes iniciar sesión y comenzar a aprender a actuar en situaciones de emergencia.\n\n"
                + "Saludos,\nEl equipo de Reacciona");
        mailSender.send(message);
    }
}
