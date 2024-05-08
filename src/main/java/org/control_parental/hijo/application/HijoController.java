package org.control_parental.hijo.application;

import jakarta.validation.Valid;
import org.apache.catalina.connector.Response;
import org.control_parental.csv.CSVHelper;
import org.control_parental.hijo.domain.Hijo;
import org.control_parental.hijo.domain.HijoService;
import org.control_parental.hijo.domain.NewHijoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
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

    @PostMapping
    public ResponseEntity<Void> createStudent(@Valid @RequestBody NewHijoDTO newHijo) {
        hijoService.newStudent(newHijo);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/csv")
    public ResponseEntity<Void> csvStudents(@RequestParam("file")MultipartFile file) throws IOException {
        if (CSVHelper.hasCSVFormat(file)) {
            hijoService.saveCSVStudents(file);
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping
    public ResponseEntity<List<Hijo>> getAllHijos() {
        List<Hijo> hijos = hijoService.getHijos();
        return ResponseEntity.ok(hijos);
    }
}