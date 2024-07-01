package org.control_parental.auth.domain;

import org.control_parental.admin.infrastructure.AdminRepository;
import org.control_parental.auth.dto.AuthJwtResponse;
import org.control_parental.auth.dto.AuthLoginRequest;
import org.control_parental.configuration.JwtService;
import org.control_parental.configuration.RandomCode;
import org.control_parental.exceptions.IllegalArgumentException;
import org.control_parental.exceptions.ResourceNotFoundException;
import org.control_parental.padre.domain.Padre;
import org.control_parental.padre.infrastructure.PadreRepository;
import org.control_parental.profesor.infrastructure.ProfesorRepository;
import org.control_parental.usuario.domain.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.control_parental.usuario.infrastructure.UsuarioRepository;

import java.util.Optional;

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

        RandomCode randomCode = new RandomCode();

//        String codigo = randomCode.generateRandomCode();
//
//        System.out.println(codigo);

        authJwtResponse.setToken(jwtService.generateToken(usuario));
//        authJwtResponse.setCode(codigo);
        return authJwtResponse;
    }


}
