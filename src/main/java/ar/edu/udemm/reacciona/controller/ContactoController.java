package ar.edu.udemm.reacciona.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/contacto")
@CrossOrigin(origins = "http://localhost:3000")
public class ContactoController {

    @Autowired
    private JavaMailSender mailSender;

    @PostMapping
    public String enviarConsulta(@RequestBody ContactoRequest request) {
        // 1. Enviar mail a soporte
        SimpleMailMessage soporteMsg = new SimpleMailMessage();
        soporteMsg.setTo("reacciona.backend@gmail.com");
        soporteMsg.setSubject("Nueva consulta de contacto");
        soporteMsg.setText("Nombre: " + request.getName() + "\nEmail: " + request.getEmail() + "\nMensaje:\n" + request.getMessage());
        mailSender.send(soporteMsg);

        // 2. Enviar mail de confirmación al usuario
        SimpleMailMessage userMsg = new SimpleMailMessage();
        userMsg.setTo(request.getEmail());
        userMsg.setSubject("Consulta recibida - Reacciona");
        userMsg.setText("¡Hola " + request.getName() + "!\n\nHemos recibido tu consulta y te responderemos a la brevedad.\n\nGracias por contactarnos.\n\nEquipo Reacciona");
        mailSender.send(userMsg);

        return "OK";
    }

    public static class ContactoRequest {
        private String name;
        private String email;
        private String message;
        // getters y setters
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
    }
}