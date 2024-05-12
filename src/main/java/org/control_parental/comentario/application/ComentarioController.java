package org.control_parental.comentario.application;

import jakarta.validation.Valid;
import jakarta.websocket.server.PathParam;
import org.control_parental.comentario.domain.ComentarioService;
import org.control_parental.comentario.dto.ComentarioResponseDto;
import org.control_parental.comentario.dto.NewComentarioDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<Void> postComentario(@Valid @RequestBody NewComentarioDto newComentarioDto) {
        comentarioService.postComentario(newComentarioDto);
        return ResponseEntity.created(null).build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> patchComentario(@PathVariable Long id,@Valid @RequestBody NewComentarioDto newComentarioDto) {
        comentarioService.patchComentario(id, newComentarioDto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComentarioById(@PathVariable Long id) {
        comentarioService.deleteComentarioById(id);
        return ResponseEntity.noContent().build();
    }
}
