/*
package org.control_parental.padre.application;

import jakarta.validation.Valid;
import org.apache.catalina.connector.Response;
import org.control_parental.hijo.domain.Hijo;
import org.control_parental.padre.domain.Padre;
import org.control_parental.padre.domain.PadreService;
import org.control_parental.padre.dto.NewPadreDto;
import org.control_parental.padre.dto.PadreResponseDto;
import org.control_parental.padre.dto.PadreSelfResponseDto;
import org.control_parental.usuario.NewPasswordDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.nio.file.AccessDeniedException;
import java.util.List;

@RestController
@RequestMapping("/padre")
public class PadreController {

    @Autowired
    PadreService padreService;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping
    ResponseEntity<Void> savePadre(@Valid @RequestBody NewPadreDto newPadreDto) {
        String location = padreService.savePadre(newPadreDto);
        URI locationHeader = URI.create(location);
        return ResponseEntity.created(locationHeader).build();
    }

    @GetMapping("/{id}")
    ResponseEntity<PadreResponseDto> getPadreById(@PathVariable Long id) {
        PadreResponseDto padreResponseDto = padreService.getPadreById(id);
        return ResponseEntity.ok(padreResponseDto);
    }
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('PADRE')")
    @GetMapping("/me")
    ResponseEntity<PadreSelfResponseDto> getPadre() {
        PadreSelfResponseDto response = padreService.getOwnInfo();
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('PADRE')")
    @DeleteMapping("/{id}")
    ResponseEntity<Void> deletePadre(@PathVariable Long id) throws AccessDeniedException {
        padreService.deletePadre(id);
        return ResponseEntity.created(null).build();
    }
    @GetMapping("/{id}/hijos")
    ResponseEntity<List<Hijo>> getHijos(@PathVariable Long id) {
        List<Hijo> hijos = padreService.getHijos(id);
        return ResponseEntity.ok(hijos);
    }

    @PreAuthorize("hasRole('PADRE')")
    @GetMapping("/myhijos")
    ResponseEntity<List<Hijo>> getMyHijos() {
    production
        List<Hijo> hijos = padreService.getOwnHijos();
        return ResponseEntity.ok(hijos);
    }

    @PreAuthorize("hasRole('PADRE')")
    @PatchMapping("/password")
    ResponseEntity<Void> newPassword(@RequestBody NewPasswordDto newPasswordDto) {
        padreService.newPassword(newPasswordDto);
        return ResponseEntity.ok().build();
    }
    
}
*/