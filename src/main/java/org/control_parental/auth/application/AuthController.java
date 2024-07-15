package org.control_parental.auth.application;

import org.control_parental.auth.domain.AuthService;
import org.control_parental.auth.dto.AuthJwtResponse;
import org.control_parental.auth.dto.AuthLoginRequest;
import org.control_parental.auth.dto.ForgottenPasswordDto;
import org.control_parental.codigoRecuperacion.domain.Codigo;
import org.control_parental.codigoRecuperacion.domain.CodigoDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthJwtResponse> login(@RequestBody AuthLoginRequest authLoginRequest){
        return ResponseEntity.ok(authService.login(authLoginRequest));
    }

    @PostMapping("/contraseña")
    public ResponseEntity<Void> codigoRecuperacion(@RequestParam String email) {
        authService.recuperarContrasenha(email);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/codigo")
    public ResponseEntity<Void> verificarCodigo(@RequestBody CodigoDto codigoDto) {
        String status = authService.verificarCodigo(codigoDto);
        if (Objects.equals(status, "200")) return ResponseEntity.ok().build();
        else return new ResponseEntity<>(HttpStatusCode.valueOf(404));
    }

    @PostMapping("/nuevaContraseña")
    public ResponseEntity<Void> nuevaContraseña(@RequestBody ForgottenPasswordDto forgottenPasswordDto) {
        authService.cambiarContraseña(forgottenPasswordDto);
        return ResponseEntity.ok().build();
    }


    /*
    @PostMapping("/register")
    public ResponseEntity<AuthJwtResponse> register(@RequestBody){

    }*/
}
