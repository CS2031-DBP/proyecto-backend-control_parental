package org.control_parental.auth.domain;

import org.control_parental.auth.dto.AuthJwtResponse;
import org.control_parental.auth.dto.AuthLoginRequest;
import org.control_parental.auth.JwtService;
import org.control_parental.auth.dto.ForgottenPasswordDto;
import org.control_parental.codigoRecuperacion.domain.Codigo;
import org.control_parental.codigoRecuperacion.domain.CodigoDto;
import org.control_parental.codigoRecuperacion.infrastructure.CodigoRepository;
import org.control_parental.configuration.RandomCode;
import org.control_parental.email.codigoVerificacion.CodigoVerificacionEmailEvent;
import org.control_parental.exceptions.IllegalArgumentException;
import org.control_parental.exceptions.ResourceNotFoundException;
import org.control_parental.usuario.domain.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.control_parental.usuario.infrastructure.UsuarioRepository;

import java.util.Date;
import java.util.Objects;

@Service
public class AuthService {

    @Autowired
    private UsuarioRepository<Usuario> usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    private CodigoRepository codigoRepository;

    public AuthJwtResponse login(AuthLoginRequest authLoginRequest) {
        Usuario usuario = usuarioRepository.findByEmail(authLoginRequest.getEmail()).orElseThrow(
                () -> new ResourceNotFoundException("El usuario no existe"));
        AuthJwtResponse authJwtResponse = new AuthJwtResponse();

        if(!passwordEncoder.matches(authLoginRequest.getPassword(), usuario.getPassword())) {
            throw new IllegalArgumentException("Incorrect Password");
        }

        // -- para las push notifications, token es creado y mapeado en el front
        usuario.setNotificationToken(authLoginRequest.getNotificationToken());

        usuarioRepository.save(usuario);

        authJwtResponse.setToken(jwtService.generateToken(usuario));
        return authJwtResponse;
    }

    public void recuperarContrasenha(String email) {
        Usuario usuario = usuarioRepository.findByEmail(email).orElseThrow(()-> new ResourceNotFoundException("El usuario no fue encontrado"));


        RandomCode randomCode = new RandomCode();
        String codigo = randomCode.generateRandomCode();
        Date date = new Date();

        Codigo newCodigo = new Codigo();
        newCodigo.setCodigo(codigo);
        newCodigo.setDate(date);
        if (usuario.getCodigo() != null) usuario.setCodigo(null);
        usuario.setCodigo(newCodigo);


        usuarioRepository.save(usuario);
        codigoRepository.save(newCodigo);
        applicationEventPublisher.publishEvent(
                new CodigoVerificacionEmailEvent(usuario.getEmail(), codigo, usuario.getNombre())
        );
    }

    public String verificarCodigo(CodigoDto codigoDto) {

        Usuario usuario = usuarioRepository.findByEmail(codigoDto.getEmail()).orElseThrow(()-> new ResourceNotFoundException("Persona no encontrada"));
        Date date = new Date();
        long differenceInMillis = date.getTime() - usuario.getCodigo().getDate().getTime();
        long tenMinutesInMillis = 10 * 60 * 1000;

        if (tenMinutesInMillis <= differenceInMillis && Objects.equals(usuario.getCodigo().getCodigo(), codigoDto.getCodigo()))
            return "200";
        else return "404";
    }

    public void cambiarContraseña(ForgottenPasswordDto forgottenPasswordDto) {
        Usuario usuario = usuarioRepository.findByEmail(forgottenPasswordDto.getEmail()).orElseThrow(()-> new ResourceNotFoundException("Usuario no encontrado"));
        usuario.setPassword(passwordEncoder.encode(forgottenPasswordDto.getPassword()));
    }

}









