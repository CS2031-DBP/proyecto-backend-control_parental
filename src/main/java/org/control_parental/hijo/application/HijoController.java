package org.control_parental.hijo.application;

import jakarta.validation.Valid;
import org.control_parental.csv.CSVHelper;
import org.control_parental.hijo.domain.Hijo;
import org.control_parental.hijo.domain.HijoService;
import org.control_parental.hijo.dto.HijoResponseDto;
import org.control_parental.hijo.dto.NewHijoDto;
import org.control_parental.publicacion.domain.Publicacion;
import org.control_parental.publicacion.dto.PublicacionResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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

    @PostMapping
    public ResponseEntity<Void> createStudent(@Valid @RequestBody NewHijoDto newHijoDto,
                                              @RequestParam Long idPadre){
        hijoService.createStudent(newHijoDto, idPadre);
        return ResponseEntity.created(null).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        hijoService.deleteHijo(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
/*
    @GetMapping("/{id}/publicaciones")
    public ResponseEntity<List<PublicacionResponseDto>> getPublicaciones(@PathVariable Long id) {
        return ResponseEntity.ok(hijoService.getPublicaciones(id));
    }
*/

    /*
    @PatchMapping("/{id}")
    public ResponseEntity<Void> updateStudent(@PathVariable Long id, @Valid @RequestBody NewHijoDto newHijo) {
        hijoService.updateStudent(id, newHijo);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<HijoResponseDto>> getAllHijos() {
        List<HijoResponseDto> hijos = hijoService.getHijos();
        return ResponseEntity.ok(hijos);
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
