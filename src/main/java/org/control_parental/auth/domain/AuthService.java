package org.control_parental.auth.domain;

import org.control_parental.auth.dto.AuthJwtResponse;
import org.control_parental.auth.dto.AuthLoginRequest;
import org.control_parental.auth.JwtService;
import org.control_parental.configuration.RandomCode;
import org.control_parental.exceptions.IllegalArgumentException;
import org.control_parental.exceptions.ResourceNotFoundException;
import org.control_parental.usuario.domain.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.control_parental.usuario.infrastructure.UsuarioRepository;

@Service
public class AuthService {

    @Autowired
    private UsuarioRepository<Usuario> usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtService jwtService;

    public AuthJwtResponse login(AuthLoginRequest authLoginRequest) {
        Usuario usuario = usuarioRepository.findByEmail(authLoginRequest.getEmail()).orElseThrow(
                () -> new ResourceNotFoundException("El usuario no existe"));
        AuthJwtResponse authJwtResponse = new AuthJwtResponse();

        if(!passwordEncoder.matches(authLoginRequest.getPassword(), usuario.getPassword())) {
            throw new IllegalArgumentException("Incorrect Password");
        }

        // -- para las push notifications, token es creado y mapeado en el front
        usuario.setNotificationToken(authLoginRequest.getNotificationToken());

        authJwtResponse.setToken(jwtService.generateToken(usuario));
        return authJwtResponse;
    }


}
