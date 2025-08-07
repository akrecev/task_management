package ru.kretsev.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.kretsev.dto.error.ErrorResponse;
import ru.kretsev.service.LoggingService;

/**
 * Filter for processing JWT-based authentication in incoming requests.
 * Extracts and validates JWT tokens from the Authorization header.
 */
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final LoggingService loggingService;
    private final ObjectMapper objectMapper;

    private static final String BEARER_PREFIX = "Bearer ";
    private static final String CONTENT_TYPE_JSON = "application/json; charset=UTF-8";

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        loggingService.logDebug("Processing request to {}", request.getRequestURI());

        if (authHeader == null || !authHeader.startsWith(BEARER_PREFIX)) {
            loggingService.logDebug("No valid Authorization header, proceeding with filter chain");
            filterChain.doFilter(request, response);
            return;
        }

        final String jwt = authHeader.substring(BEARER_PREFIX.length());
        loggingService.logDebug("JWT token extracted from the request");

        try {
            final String userEmail = jwtService.getUsername(jwt);
            loggingService.logDebug("Extracted user email from JWT");

            if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);
                loggingService.logDebug("Loaded user details for: {}", userEmail);

                if (jwtService.isTokenValid(jwt, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                    loggingService.logDebug("Authentication set for user: {}", userEmail);
                }
            }
            filterChain.doFilter(request, response);
        } catch (ExpiredJwtException e) {
            loggingService.logWarn("Expired JWT token: {}", e.getMessage());
            sendErrorResponse(response, "Токен истёк");
        } catch (MalformedJwtException | SignatureException e) {
            loggingService.logWarn("Invalid JWT token: {}", e.getMessage());
            sendErrorResponse(response, "Неверный токен");
        } catch (IllegalArgumentException e) {
            loggingService.logWarn("Invalid JWT token format: {}", e.getMessage());
            sendErrorResponse(response, "Неверный формат токена");
        }
    }

    /**
     * Sends an error response to the client with the specified message and HTTP status.
     *
     * @param response the HTTP response object
     * @param message  the error message to send
     * @throws IOException if an I/O error occurs
     */
    private void sendErrorResponse(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(CONTENT_TYPE_JSON);
        response.setCharacterEncoding("UTF-8");
        ErrorResponse errorResponse = new ErrorResponse(message, HttpStatus.UNAUTHORIZED.value());
        try {
            String json = objectMapper.writeValueAsString(errorResponse);
            loggingService.logDebug("Sending error response: {}", json);
            response.getWriter().write(json);
            response.getWriter().flush();
        } catch (IOException e) {
            loggingService.logError("Error in generating a response: {}", e.getMessage(), e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Ошибка на сервере");
        }
    }
}
