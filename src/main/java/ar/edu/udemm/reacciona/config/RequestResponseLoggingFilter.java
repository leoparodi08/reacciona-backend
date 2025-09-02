package ar.edu.udemm.reacciona.config;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
public class RequestResponseLoggingFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(RequestResponseLoggingFilter.class);

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, jakarta.servlet.ServletException {
        if (request instanceof HttpServletRequest && response instanceof HttpServletResponse) {
            HttpServletRequest httpRequest = (HttpServletRequest) request;
            HttpServletResponse httpResponse = (HttpServletResponse) response;

            // Wrap request and response to cache their content
            ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper(httpRequest);
            ContentCachingResponseWrapper wrappedResponse = new ContentCachingResponseWrapper(httpResponse);

            // Log the request path
            logger.info("Request Method: {}, Request Path: {}", wrappedRequest.getMethod(), wrappedRequest.getRequestURI());
            // Proceed with the filter chain
            chain.doFilter(wrappedRequest, wrappedResponse);

            if ("GET".equals(wrappedRequest.getMethod())) {
                logger.info("GET requests do not have a body, skipping request body logging.");
            } else {
                String requestBody = new String(wrappedRequest.getContentAsByteArray(), StandardCharsets.UTF_8);
                logger.info("Request Body: {}", requestBody);
            }


            // Log the response body
            String responseBody = new String(wrappedResponse.getContentAsByteArray(), StandardCharsets.UTF_8);
            logger.info("Response Body: {}", responseBody);

            // Copy the response content back to the original response
            wrappedResponse.copyBodyToResponse();
        } else {
            chain.doFilter(request, response);
        }
    }
}