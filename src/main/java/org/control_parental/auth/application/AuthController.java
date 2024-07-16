package org.control_parental.auth.application;

import org.control_parental.auth.domain.AuthService;
import org.control_parental.auth.dto.AuthJwtResponse;
import org.control_parental.auth.dto.AuthLoginRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthJwtResponse> login(@RequestBody AuthLoginRequest authLoginRequest){
        return ResponseEntity.ok(authService.login(authLoginRequest));
    }
    /*
    @PostMapping("/register")
    public ResponseEntity<AuthJwtResponse> register(@RequestBody){

    }*/

    @GetMapping("/check")
    public ResponseEntity<Void> check() {
        return ResponseEntity.ok().build();
    }



}
