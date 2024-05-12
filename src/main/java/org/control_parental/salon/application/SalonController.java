package org.control_parental.salon.application;

import org.control_parental.hijo.dto.NewHijoDto;
import org.control_parental.hijo.dto.HijoResponseDto;
import org.control_parental.salon.dto.NewSalonDTO;
import org.control_parental.salon.domain.Salon;
import org.control_parental.salon.domain.SalonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/salon")
public class SalonController {
    @Autowired
    private SalonService salonService;

    @PostMapping
    public ResponseEntity<Salon> createSalon( @RequestBody NewSalonDTO newSalonDTO) {
        Salon salon = salonService.createSalon(newSalonDTO);
        return new ResponseEntity<>(salon, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Salon> getSalon(@PathVariable Long id) {
        Salon salon = salonService.getSalonById(id);
        return ResponseEntity.ok(salon);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Salon> addStudent(@PathVariable Long id, @RequestBody NewHijoDto hijoDTO) {
        salonService.addStudentToSalon(id, hijoDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{id}/hijos")
    public ResponseEntity<List<HijoResponseDto>> getStudents(@PathVariable Long id) {
        List<HijoResponseDto> hijos = salonService.getAllStudents(id);
        return ResponseEntity.ok(hijos);
    }

}
