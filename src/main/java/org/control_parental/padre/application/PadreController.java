package org.control_parental.padre.application;

import jakarta.validation.Valid;
import org.control_parental.padre.domain.Padre;
import org.control_parental.padre.domain.PadreService;
import org.control_parental.padre.dto.NewPadreDto;
import org.control_parental.padre.dto.PadreResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/padre")
public class PadreController {

    @Autowired
    PadreService padreService;

    @PostMapping
    ResponseEntity<Void> savePadre(@Valid @RequestBody NewPadreDto newPadreDto) {
        padreService.savePadre(newPadreDto);
        return ResponseEntity.created(null).build();
    }

    @GetMapping("/{id}")
    ResponseEntity<PadreResponseDto> getPadreById(@PathVariable Long id) {
        PadreResponseDto padreResponseDto = padreService.getPadreById(id);
        return ResponseEntity.ok(padreResponseDto);
    }
}
