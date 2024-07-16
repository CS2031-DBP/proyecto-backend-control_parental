
package org.control_parental.padre.application;

import jakarta.validation.Valid;
import org.apache.catalina.connector.Response;
import org.control_parental.csv.CSVHelper;
import org.control_parental.hijo.domain.Hijo;
import org.control_parental.padre.domain.Padre;
import org.control_parental.padre.domain.PadreService;
import org.control_parental.padre.dto.NewPadreDto;
import org.control_parental.padre.dto.PadreResponseDto;
import org.control_parental.padre.dto.PadreSelfResponseDto;
import org.control_parental.usuario.NewPasswordDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
    public ResponseEntity<Void> savePadre(@Valid @RequestBody NewPadreDto newPadreDto) {
        String location = padreService.savePadre(newPadreDto);
        URI locationHeader = URI.create(location);
        return ResponseEntity.created(locationHeader).build();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(value = "/csv", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Void> saveCSvPadre(@RequestParam("file")MultipartFile file) throws IOException {
        if (CSVHelper.hasCSVFormat(file)) {
            padreService.savePadresCsv(file);
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
    //@PreAuthorize("hasAnyRole()")
    @GetMapping("/{id}")
    public ResponseEntity<PadreResponseDto> getPadreById(@PathVariable Long id) {
        PadreResponseDto padreResponseDto = padreService.getPadreById(id);
        return ResponseEntity.ok(padreResponseDto);
    }

    @GetMapping("/all")
    public ResponseEntity<List<PadreResponseDto>> getAllPadres(@RequestParam int page, @RequestParam int size) {
        return ResponseEntity.ok(padreService.getAllPadres(page, size));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_PADRE')")
    @GetMapping("/me")
    public ResponseEntity<PadreSelfResponseDto> getPadre() {
        PadreSelfResponseDto response = padreService.getOwnInfo();
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePadre(@PathVariable Long id) throws AccessDeniedException {
        padreService.deletePadre(id);
        return ResponseEntity.noContent().build();
    }
    @PreAuthorize("hasRole('ROLE_PROFESOR')")
    @GetMapping("/{id}/hijos")
    public ResponseEntity<List<Hijo>> getHijos(@PathVariable Long id) {
        List<Hijo> hijos = padreService.getHijos(id);
        return ResponseEntity.ok(hijos);
    }

    @PreAuthorize("hasRole('ROLE_PADRE')")
    @GetMapping("/myhijos")
    public ResponseEntity<List<Hijo>> getMyHijos() {
        List<Hijo> hijos = padreService.getOwnHijos();
        return ResponseEntity.ok(hijos);
    }

    @PreAuthorize("hasRole('ROLE_PADRE')")
    @GetMapping("/liked")
    public ResponseEntity<List<Long>> getAllLiked() {
        List<Long> liked = padreService.getLiked();
        return ResponseEntity.ok(liked);
    }

    @PreAuthorize("hasRole('ROLE_PADRE')")
    @PatchMapping("/password")
    public ResponseEntity<Void> newPassword(@RequestBody NewPasswordDto newPasswordDto) throws AccessDeniedException {
        padreService.newPassword(newPasswordDto);
        return ResponseEntity.ok().build();
    }

}
