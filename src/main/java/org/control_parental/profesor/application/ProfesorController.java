package org.control_parental.profesor.application;

import jakarta.validation.Valid;
import org.control_parental.profesor.domain.ProfesorService;
import org.control_parental.profesor.dto.NewProfesorDto;
import org.control_parental.profesor.dto.ProfesorResponseDto;
import org.control_parental.profesor.dto.ProfesorSelfResponseDto;
import org.control_parental.publicacion.domain.Publicacion;
import org.control_parental.usuario.NewPasswordDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/profesor")
public class ProfesorController {

    @Autowired
    ProfesorService profesorService;



    @GetMapping("/{id}")
    public ResponseEntity<ProfesorResponseDto> getSmallDetailProfesor(@PathVariable Long id) {
        ProfesorResponseDto response = profesorService.getProfesorRepsonseDto(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/me")
    public ResponseEntity<ProfesorSelfResponseDto> getProfesor() {
        ProfesorSelfResponseDto profesorSelfResponseDto = profesorService.getOwnProfesorInfo();
        return ResponseEntity.ok(profesorSelfResponseDto);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteProfesor(@PathVariable Long id) {
        profesorService.deleteProfesor(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/password")
    public ResponseEntity<Void> newPassword(@Valid @RequestBody NewPasswordDto newPasswordDto) {
        profesorService.patchPassword(newPasswordDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("{id}/publicaciones")
    public ResponseEntity<List<Publicacion>> getPublicacionesProfesor(@PathVariable Long id) {
        List<Publicacion> publicaciones = profesorService.getPublicaciones(id);
        return ResponseEntity.ok(publicaciones);
    }

    @PostMapping
    public ResponseEntity<Void> createProfesor(@Valid @RequestBody NewProfesorDto newProfesorDTO) {
        profesorService.newProfesor(newProfesorDTO);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

}

