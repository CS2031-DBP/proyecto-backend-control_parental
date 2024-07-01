package org.control_parental.configuration;

import org.control_parental.exceptions.ResourceNotFoundException;
import org.control_parental.usuario.domain.Usuario;
import org.control_parental.usuario.infrastructure.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.nio.file.AccessDeniedException;
import java.util.Objects;

@Component
public class AuthorizationUtils {

    @Autowired
    UsuarioRepository<Usuario> usuarioRepository;

    @Autowired
    PasswordEncoder encoder;

    public String authenticateUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            return authentication.getName();
        } else {
            throw new IllegalStateException("User not authenticated");
        }
    }

    public void verifyUserAuthorization(String userEmail, Long id) throws AccessDeniedException {
        Usuario usuarioEmail = usuarioRepository.findByEmail(userEmail).orElseThrow(
                ()-> new ResourceNotFoundException("Usuario no encontrado"));
        Usuario usuarioId = usuarioRepository.findById(id).orElseThrow(
                ()-> new ResourceNotFoundException("Usuario no encontrado"));
        if (!Objects.equals(usuarioEmail.getEmail(), usuarioId.getEmail()) && !Objects.equals(usuarioEmail.getRole().toString(), "ADMIN"))
            throw new AccessDeniedException("No estas autorizado");
    }
}
