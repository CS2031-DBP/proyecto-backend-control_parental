package org.control_parental.profesor.application;

import jakarta.validation.Valid;
import org.control_parental.csv.CSVHelper;
import org.control_parental.profesor.domain.ProfesorService;
import org.control_parental.profesor.dto.NewProfesorDto;
import org.control_parental.profesor.dto.ProfesorResponseDto;
import org.control_parental.profesor.dto.ProfesorSelfResponseDto;
import org.control_parental.publicacion.domain.Publicacion;
import org.control_parental.usuario.NewPasswordDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.util.List;

@RestController
@RequestMapping("/profesor")
public class ProfesorController {

    @Autowired
    ProfesorService profesorService;

    @GetMapping("/{id}")
    public ResponseEntity<ProfesorResponseDto> getSmallDetailProfesor(@PathVariable Long id) {
        return ResponseEntity.ok(profesorService.getProfesorRepsonseDto(id));
    }
    @GetMapping("/salon/{salonId}")
    public ResponseEntity<List<ProfesorResponseDto>> getAllProfesoresBySalon(@PathVariable Long salonId) {
        return ResponseEntity.ok(profesorService.getProfessorBySalon(salonId));
    }
    @GetMapping("/all")
    public ResponseEntity<List<ProfesorResponseDto>> getAllProfessors(@RequestParam int page, @RequestParam int size) {
        return ResponseEntity.ok(profesorService.getAllProfesores(page, size));
    }

    @PreAuthorize("hasRole('ROLE_PROFESOR') or hasRole('ROLE_ADMIN')")
    @GetMapping("/me")
    public ResponseEntity<ProfesorSelfResponseDto> getProfesor() {
        return ResponseEntity.ok(profesorService.getOwnProfesorInfo());
    }
    @PreAuthorize("hasRole('ROLE_PROFESOR') or hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProfesor(@PathVariable Long id) throws AccessDeniedException {
        profesorService.deleteProfesor(id);
        return ResponseEntity.noContent().build();
    }
    @PreAuthorize("hasRole('ROLE_PROFESOR') or hasRole('ROLE_ADMIN')")
    @PatchMapping("/{id}")
    public ResponseEntity<Void> updateProfesor(@PathVariable Long id,@RequestBody @Valid NewProfesorDto newProfesorDto) {
        profesorService.updateProfesor(id, newProfesorDto);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('ROLE_PROFESOR') or hasRole('ROLE_ADMIN') or hasRole('ROLE_PADRE')")
    @GetMapping("{id}/publicaciones")
    public ResponseEntity<List<Publicacion>> getPublicacionesProfesor(@PathVariable Long id) {
        List<Publicacion> publicaciones = profesorService.getPublicaciones(id);
        return ResponseEntity.ok(publicaciones);
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<Void> createProfesor(@Valid @RequestBody NewProfesorDto newProfesorDTO) {
        String url = profesorService.newProfesor(newProfesorDTO);
        URI locationHeader = URI.create(url);
        return ResponseEntity.created(locationHeader).build();
    }
    @PreAuthorize("hasRole('ROLE_PROFESOR') or hasRole('ROLE_ADMIN')")
    @PatchMapping("/password")
    public ResponseEntity<Void> newPassword(@Valid @RequestBody NewPasswordDto newPasswordDto) {
        profesorService.patchPassword(newPasswordDto);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('ROLE_PROFESOR')")
    @PostMapping(value = "/csv", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Void> saveCscProfesores(@RequestParam("file") MultipartFile file) throws IOException {
        if (CSVHelper.hasCSVFormat(file)) {
            profesorService.saveCsvProfesores(file);
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}

