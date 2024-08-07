package org.control_parental.salon.application;

import org.apache.catalina.connector.Response;
import org.control_parental.hijo.dto.ReducedHijoDto;
import org.control_parental.publicacion.dto.PublicacionResponseDto;
import org.control_parental.salon.dto.NewSalonDTO;
import org.control_parental.salon.domain.SalonService;
import org.control_parental.salon.dto.SalonResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/salon")
public class SalonController {
    @Autowired
    private SalonService salonService;

    @GetMapping("/{id}")
    public ResponseEntity<SalonResponseDto> getSalon(@PathVariable Long id) {
        return ResponseEntity.ok(salonService.getSalonById(id));
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<Void> createSalon(@RequestBody NewSalonDTO newSalonDTO) {
        String location = salonService.createSalon(newSalonDTO);
        URI locationHeader = URI.create(location);
        return ResponseEntity.created(locationHeader).build();
    }

    @GetMapping("/all")
    public ResponseEntity<List<SalonResponseDto>> getAllSalones(@RequestParam int page, @RequestParam int size) {
        return ResponseEntity.ok(salonService.getAllSalones(page, size));
    }

    @GetMapping("/{id}/hijos")
    public ResponseEntity<List<ReducedHijoDto>> getStudents(@PathVariable Long id) {
        List<ReducedHijoDto> hijos = salonService.getAllStudents(id);
        return ResponseEntity.ok(hijos);
    }

    @GetMapping("{id}/publicaciones")
    public ResponseEntity<List<PublicacionResponseDto>> getPublicaciones(@PathVariable Long id) {
        List<PublicacionResponseDto> publicaciones = salonService.getAllPublicaciones(id);
        return ResponseEntity.ok(publicaciones);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PatchMapping("/{idSalon}/hijo/{idHijo}")
    public ResponseEntity<Void> addHijo(@PathVariable Long idSalon, @PathVariable Long idHijo) {
        salonService.addHijo(idSalon, idHijo);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PatchMapping("/{idSalon}/profesor/{idProfesor}")
    public ResponseEntity<Void> addProfesor(@PathVariable Long idSalon, @PathVariable Long idProfesor) {
        salonService.addProfesor(idSalon, idProfesor);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{salonId}/quitarProfesor/{idProfesor}")
    public ResponseEntity<Void> removeProfesor(@PathVariable Long salonId, @PathVariable Long idProfesor) {
        salonService.deleteProfesorFromSalon(salonId, idProfesor);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{salonId}/quitarHijo/{hijoId}")
    public ResponseEntity<Void> removeStudent(@PathVariable Long salonId, @PathVariable Long hijoId) {
        salonService.deleteStudentFromSalon(salonId, hijoId);
        return ResponseEntity.ok().build();
    }
}

















