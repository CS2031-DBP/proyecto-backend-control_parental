package org.control_parental.publicacion.application;

import org.control_parental.publicacion.dto.NewPublicacionDto;
import org.control_parental.publicacion.dto.PublicacionResponseDto;
import org.control_parental.publicacion.domain.PublicacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("/posts")
public class PublicacionController {

    @Autowired
    PublicacionService publicacionService;

    @GetMapping("/{id}")
    public ResponseEntity<PublicacionResponseDto> getPublicacion(@PathVariable Long id) {
        return ResponseEntity.ok(publicacionService.getPublicacionById(id));
    }

    @PostMapping
    public ResponseEntity<Void> postPublicacion(@RequestBody NewPublicacionDto newPublicacion) {
        publicacionService.savePublicacion(newPublicacion);
        return ResponseEntity.created(null).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePublicacion(@PathVariable Long id) {
        publicacionService.deletePublicacion(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> patchPublicacion(@PathVariable Long id, @RequestBody NewPublicacionDto newPublicacion) {
        publicacionService.patchPublicacion(id, newPublicacion);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{salon_id}")
    public ResponseEntity<List<PublicacionResponseDto>> findPostsBySalonId(@PathVariable Long salon_id) {
        return ResponseEntity.ok(publicacionService.findPostsBySalonId(salon_id));
    }

    @PostMapping("/{salon_id}")
    public ResponseEntity<Void> createPost(@PathVariable Long salon_id, @RequestParam List<Long> hijos_id, @RequestBody NewPublicacionDto newPostData) {
        publicacionService.createPost(newPostData, salon_id, hijos_id);
        return ResponseEntity.created(null).build();
    }

}
