package ar.edu.udemm.reacciona.config;

import java.io.IOException;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest; // Importación necesaria
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CorsResponseFilter implements Filter {

    private final String FRONTEND_ALB_DOMAIN_HTTP = "http://reacciona-frontend-alb-1984260578.us-east-1.elb.amazonaws.com";
    private final String FRONTEND_ALB_DOMAIN_HTTPS = "https://reacciona-frontend-alb-1984260578.us-east-1.elb.amazonaws.com";

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        // Casteo necesario para acceder a métodos HTTP (URI, Method, Headers)
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        String origin = request.getHeader("Origin");

        // --- 1. CONFIGURACIÓN CORS (APLICADA A AMBOS OPTIONS Y SOLICITUDES REALES) ---

        // Habilitar credenciales, métodos y encabezados para TODAS las respuestas
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE, PUT, PATCH");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Headers", "Authorization, Content-Type, Origin, Accept, X-Requested-With");

        // 2. Establecer el Origen Permitido (ACAO)
        if (FRONTEND_ALB_DOMAIN_HTTP.equals(origin) || FRONTEND_ALB_DOMAIN_HTTPS.equals(origin)) {
            // Si el origen coincide (HTTP o HTTPS), lo devolvemos
            response.setHeader("Access-Control-Allow-Origin", origin);
        }
        // Nota: Si el origen no está permitido, no se envía el encabezado ACAO, lo que provoca el error de CORS (Comportamiento deseado)

        // --- 2. MANEJO DE PRE-VUELO (OPTIONS) ---

        // Si la solicitud es OPTIONS, respondemos inmediatamente y terminamos.
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK); // Devuelve 200 OK
            return; // Detiene el procesamiento de la solicitud aquí
        }

        // --- 3. PROCESAR SOLICITUD REAL ---

        // Para POST, GET, etc., permite que el request continúe hacia los controladores.
        chain.doFilter(req, res);
    }
}