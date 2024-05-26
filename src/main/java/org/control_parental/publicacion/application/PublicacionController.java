package org.control_parental.publicacion.application;

import org.control_parental.publicacion.dto.NewPublicacionDto;
import org.control_parental.publicacion.dto.PublicacionResponseDto;
import org.control_parental.publicacion.domain.PublicacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/publicacion")
public class PublicacionController {

    @Autowired
    PublicacionService publicacionService;

    @GetMapping("/{id}")
    public ResponseEntity<PublicacionResponseDto> getPublicacion(@PathVariable Long id) {
        return ResponseEntity.ok(publicacionService.getPublicacionById(id));
    }

    @PreAuthorize("hasRole('ROLE_PROFESOR') or hasRole('ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<Void> postPublicacion(@RequestBody NewPublicacionDto newPublicacion) {
        publicacionService.savePublicacion(newPublicacion);
        return ResponseEntity.created(null).build();
    }

    @PreAuthorize("hasRole('ROLE_PROFESOR') or hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePublicacion(@PathVariable Long id) {
        publicacionService.deletePublicacion(id);
        return ResponseEntity.noContent().build();
    }

//    @PreAuthorize("hasRole('ROLE_PADRE')")
    @PostMapping("/like/{postId}")
    public ResponseEntity<Void> like(@PathVariable Long postId) {
        publicacionService.likePost(postId);
        return ResponseEntity.created(null).build();
    }
//    @PreAuthorize("hasRole('ROLE_PADRE')")
    @DeleteMapping("/like/{postId}")
    public ResponseEntity<Void> quitarLike(@PathVariable Long postId) {
        publicacionService.deLikePost(postId);
        return ResponseEntity.noContent().build();
    }
/*
    @PatchMapping("/{id}")
    public ResponseEntity<Void> patchPublicacion(@PathVariable Long id, @RequestBody NewPublicacionDto newPublicacion) {
        publicacionService.patchPublicacion(id, newPublicacion);
        return ResponseEntity.ok().build();
    }
*/
    /*
    @GetMapping("/{salon_id}")
    public ResponseEntity<List<PublicacionResponseDto>> findPostsBySalonId(@PathVariable Long salon_id) {
        return ResponseEntity.ok(publicacionService.findPostsBySalonId(salon_id));
    }
    */

/*
    @PostMapping("/{salon_id}")
    public ResponseEntity<Void> createPost(@PathVariable Long salon_id, @RequestParam List<Long> hijos_id, @RequestBody NewPublicacionDto newPostData) {
        publicacionService.createPost(newPostData, salon_id, hijos_id);
        return ResponseEntity.created(null).build();
    }
*/
}
