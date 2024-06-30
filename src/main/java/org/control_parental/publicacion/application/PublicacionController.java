package org.control_parental.publicacion.application;

import org.control_parental.publicacion.dto.NewPublicacionDto;
import org.control_parental.publicacion.dto.PublicacionResponseDto;
import org.control_parental.publicacion.domain.PublicacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.nio.file.AccessDeniedException;
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
        String location = publicacionService.savePublicacion(newPublicacion);
        URI locationHeader = URI.create(location);
        return ResponseEntity.created(locationHeader).build();
    }

    @PreAuthorize("hasRole('ROLE_PROFESOR') or hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePublicacion(@PathVariable Long id) throws AccessDeniedException {
        publicacionService.deletePublicacion(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ROLE_PADRE')")
    @PostMapping("/like/{postId}")
    public ResponseEntity<Void> like(@PathVariable Long postId) {
        publicacionService.likePost(postId);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('ROLE_PADRE')")
    @DeleteMapping("/like/{postId}")
    public ResponseEntity<Void> quitarLike(@PathVariable Long postId) {
        publicacionService.deLikePost(postId);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<PublicacionResponseDto>> getPublicaciones(@RequestParam int page, @RequestParam int size) {
        return ResponseEntity.ok(publicacionService.findPostsForPadre(page, size));
    }

    /*@PatchMapping("/{id}")
    public ResponseEntity<Void> patchPublicacion(@PathVariable Long id, @RequestBody NewPublicacionDto newPublicacion) {
        publicacionService.patchPublicacion(id, newPublicacion);
        return ResponseEntity.ok().build();
    }*/

    /*
    @GetMapping("/{salon_id}")
    public ResponseEntity<List<PublicacionResponseDto>> findPostsBySalonId(@PathVariable Long salon_id) {
        return ResponseEntity.ok(publicacionService.findPostsBySalonId(salon_id));
    }
    */

}
