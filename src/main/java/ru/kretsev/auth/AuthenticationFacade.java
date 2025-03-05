package ru.kretsev.auth;

import java.util.Collections;
import java.util.List;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

/**
 * Facade for accessing authentication-related information from the security context.
 */
@Component
public class AuthenticationFacade {

    /**
     * Retrieves the current authentication object from the security context.
     *
     * @return the current {@link Authentication} object, or null if not authenticated
     */
    public Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    /**
     * Retrieves the email of the currently authenticated user.
     *
     * @return the email of the current user, or null if not authenticated
     */
    public String getCurrentUserEmail() {
        Authentication authentication = getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails userDetails) {
            return userDetails.getUsername();
        }
        return null;
    }

    /**
     * Retrieves the roles of the currently authenticated user.
     *
     * @return a list of role names, or an empty list if not authenticated
     */
    public List<String> getCurrentUserRoles() {
        Authentication authentication = getAuthentication();
        if (authentication != null) {
            return authentication.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .toList();
        }
        return Collections.emptyList();
    }
}
