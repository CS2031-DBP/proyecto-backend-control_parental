package org.control_parental.publicacion.application;

import org.control_parental.publicacion.domain.NewPublicacionDTO;
import org.control_parental.publicacion.domain.PostRequestDTO;
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
    public ResponseEntity<List<PostRequestDTO>> findPostsBySalonId(@PathVariable Long salon_id) {
        return ResponseEntity.ok(service.findPostsBySalonId(salon_id));
    }

    @PostMapping("/{salon_id}")
    public ResponseEntity<Void> createPost(@PathVariable Long salon_id, @RequestParam List<Long> hijos_id, @RequestBody NewPublicacionDTO newPostData) {
        service.createPost(newPostData, salon_id, hijos_id);
        return ResponseEntity.created(null).build();
    }

}