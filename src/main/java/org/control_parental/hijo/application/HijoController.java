package org.control_parental.hijo.application;

import jakarta.validation.Valid;
import org.control_parental.csv.CSVHelper;
import org.control_parental.hijo.domain.HijoService;
import org.control_parental.hijo.dto.HijoResponseDto;
import org.control_parental.hijo.dto.NewHijoDto;
import org.control_parental.publicacion.dto.PublicacionResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/hijo")
public class HijoController {

    @Autowired
    private HijoService hijoService;


    @GetMapping("/{id}")
    public ResponseEntity<HijoResponseDto> getStudentById(@PathVariable Long id) {
        return ResponseEntity.ok(hijoService.getStudentById(id));
    }

    @GetMapping("/all")
    public ResponseEntity<List<HijoResponseDto>> getAllStudents(@RequestParam int page, @RequestParam int size) {
        return ResponseEntity.ok(hijoService.getAllStudents(page, size));
    }

    @GetMapping("/salon/{salonId}")
    public ResponseEntity<List<HijoResponseDto>> getStudentsBySalon(@PathVariable Long salonId, @RequestParam int page, @RequestParam int size) {
        return ResponseEntity.ok(hijoService.getStudentsBySalon(salonId, page, size));
    }


    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<Void> createStudent(@Valid @RequestBody NewHijoDto newHijoDto,
                                              @RequestParam Long idPadre){
        String location = hijoService.createStudent(newHijoDto, idPadre);
        URI locationHeader = URI.create(location);
        return ResponseEntity.created(locationHeader).build();
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        hijoService.deleteHijo(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{id}/publicaciones")
    public ResponseEntity<List<PublicacionResponseDto>> getPublicaciones(@PathVariable Long id, @RequestParam int page, @RequestParam int size) {
        return ResponseEntity.ok(hijoService.getPublicaciones(id, page, size));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PatchMapping("/{id}")
    public ResponseEntity<Void> updateStudent(@PathVariable Long id, @Valid @RequestBody NewHijoDto newHijo) {
        hijoService.updateStudent(id, newHijo);
        return ResponseEntity.ok().build();
    }
    
    @PostMapping(value = "/csv", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Void> csvStudents(@RequestParam("file")MultipartFile file) throws IOException {
        if (CSVHelper.hasCSVFormat(file)) {
            hijoService.saveCSVStudents(file);
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }



}
