package org.control_parental.comentario.application;

import jakarta.validation.Valid;
import org.control_parental.comentario.domain.Comentario;
import org.control_parental.comentario.domain.ComentarioService;
import org.control_parental.comentario.dto.ComentarioResponseDto;
import org.control_parental.comentario.dto.NewComentarioDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.nio.file.AccessDeniedException;
import java.util.List;

@RestController
@RequestMapping("/comentario")
public class ComentarioController {
    @Autowired
    ComentarioService comentarioService;

    @GetMapping("/{id}")
    public ResponseEntity<ComentarioResponseDto> getComentarioById(@PathVariable Long id) {
        return ResponseEntity.ok(comentarioService.getComentarioById(id));
    }

    @PostMapping
    public ResponseEntity<Void> postComentario(@Valid @RequestBody NewComentarioDto newComentarioDto,
                                               @RequestParam Long PublicacionId) {
        String location =  comentarioService.postComentario(newComentarioDto, PublicacionId);
        URI locationHeader = URI.create(location);
        return ResponseEntity.created(locationHeader).build();
    }


    @PreAuthorize("hasRole('ROLE_PADRE')")
    @PatchMapping("/{id}")
    public ResponseEntity<Void> patchComentario(@PathVariable Long id,@Valid @RequestBody NewComentarioDto newComentarioDto) throws AccessDeniedException {
        comentarioService.patchComentario(id, newComentarioDto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComentarioById(@PathVariable Long id) throws AccessDeniedException {
        comentarioService.deleteComentarioById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/publicacion/{id}")
    public ResponseEntity<List<ComentarioResponseDto>> getByPublicacionId(@PathVariable Long id, @RequestParam int page, @RequestParam int size) {
        List<ComentarioResponseDto> comentariosData = comentarioService.getByPublicacionId(id, page, size);
        return ResponseEntity.ok(comentariosData);
    }
}
