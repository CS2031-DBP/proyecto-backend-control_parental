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
    PublicacionService service;

    @GetMapping("/{salon_id}")
    public ResponseEntity<List<PublicacionResponseDto>> findPostsBySalonId(@PathVariable Long salon_id) {
        return ResponseEntity.ok(service.findPostsBySalonId(salon_id));
    }

    @PostMapping("/{salon_id}")
    public ResponseEntity<Void> createPost(@PathVariable Long salon_id, @RequestParam List<Long> hijos_id, @RequestBody NewPublicacionDto newPostData) {
        service.createPost(newPostData, salon_id, hijos_id);
        return ResponseEntity.created(null).build();
    }

}
