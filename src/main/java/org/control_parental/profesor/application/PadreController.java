package org.control_parental.profesor.application;

import jakarta.validation.Valid;
import org.control_parental.profesor.domain.Padre;
import org.control_parental.profesor.domain.PadreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/padre")
public class PadreController {

    @Autowired
    PadreService padreService;

    @PostMapping
    ResponseEntity<Void> savePadre(@Valid @RequestBody Padre padre) {
        padreService.savePadre(padre);
        return ResponseEntity.created(null).build();
    }

    @GetMapping("/{id}")
    ResponseEntity<Padre> getPadreById(@PathVariable Long id) {
        Padre padre = padreService.getPadreById(id);
        return ResponseEntity.ok(padre);
    }
}
