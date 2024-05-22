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
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/padre")
public class PadreController {

    @Autowired
    PadreService padreService;


    @PostMapping
    ResponseEntity<Void> savePadre(@Valid @RequestBody NewPadreDto newPadreDto) {
        padreService.savePadre(newPadreDto);
        return new ResponseEntity<>(HttpStatusCode.valueOf(201));
    }

    @GetMapping("/{id}")
    ResponseEntity<PadreResponseDto> getPadreById(@PathVariable Long id) {
        PadreResponseDto padreResponseDto = padreService.getPadreById(id);
        return ResponseEntity.ok(padreResponseDto);
    }

    @GetMapping("/me")
    ResponseEntity<PadreSelfResponseDto> getPadre() {
        PadreSelfResponseDto response = padreService.getOwnInfo();
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Void> deletePadre(@PathVariable Long id) {
        padreService.deletePadre(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/hijos")
    ResponseEntity<List<Hijo>> getHijos(@PathVariable Long id) {
        List<Hijo> hijos = padreService.getHijos(id);
        return ResponseEntity.ok(hijos);
    }

    @GetMapping("/{me}/hijos")
    ResponseEntity<List<Hijo>> getHijos() {
        List<Hijo> hijos = padreService.getOwnHijos();
        return ResponseEntity.ok(hijos);
    }

    @PatchMapping("/password")
    ResponseEntity<Void> newPassword(@RequestBody NewPasswordDto newPasswordDto) {
        padreService.newPassword(newPasswordDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping()
    ResponseEntity<List<Padre>> getAllPadres() {
        return ResponseEntity.ok(padreService.getAllPadres());
    }
    
}
