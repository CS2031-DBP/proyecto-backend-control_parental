package org.control_parental.usuario.application;

import org.control_parental.usuario.domain.Usuario;
import org.control_parental.usuario.domain.UsuarioService;
import org.control_parental.usuario.dto.UsuarioResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/usuario")
public class UsuarioController {
    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    public ResponseEntity<List<UsuarioResponseDto>> findAll() {
        return ResponseEntity.ok(usuarioService.getAllUsuarios());
    }
}
