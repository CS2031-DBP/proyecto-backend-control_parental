package org.control_parental.salon.application;

import org.control_parental.hijo.dto.NewHijoDto;
import org.control_parental.hijo.dto.HijoResponseDto;
import org.control_parental.publicacion.domain.Publicacion;
import org.control_parental.publicacion.dto.PublicacionResponseDto;
import org.control_parental.salon.dto.NewSalonDTO;
import org.control_parental.salon.domain.Salon;
import org.control_parental.salon.domain.SalonService;
import org.control_parental.salon.dto.SalonResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @PostMapping
    public ResponseEntity<Void> createSalon( @RequestBody NewSalonDTO newSalonDTO) {
        String location = salonService.createSalon(newSalonDTO);
        URI locationHeader= URI.create(location);
        return ResponseEntity.created(locationHeader).build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> addStudent(@PathVariable Long id, @RequestBody NewHijoDto hijoDTO) {
        salonService.addStudentToSalon(id, hijoDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{id}/hijos")
    public ResponseEntity<List<HijoResponseDto>> getStudents(@PathVariable Long id) {
        List<HijoResponseDto> hijos = salonService.getAllStudents(id);
        return ResponseEntity.ok(hijos);
    }

    @GetMapping("{id}/publicaciones")
    public ResponseEntity<List<PublicacionResponseDto>> getPublicaciones(@PathVariable Long id) {
        List<PublicacionResponseDto> publicaciones = salonService.getAllPublicaciones(id);
        return ResponseEntity.ok(publicaciones);
    }

}
