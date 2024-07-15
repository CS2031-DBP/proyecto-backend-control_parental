package org.control_parental.reply.application;

import jakarta.validation.Valid;
import org.control_parental.comentario.dto.ComentarioResponseDto;
import org.control_parental.reply.domain.ReplyService;
import org.control_parental.reply.dto.ReplyResponseDto;
import org.control_parental.reply.dto.NewReplyDto;
import org.control_parental.reply.dto.ReplySmallResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.nio.file.AccessDeniedException;
import java.util.List;

@RestController
@RequestMapping("/reply")
public class ReplyController {
    @Autowired
    ReplyService replyService;

    @GetMapping("/{id}")
    public ResponseEntity<ReplyResponseDto> getReplyById(@PathVariable Long id) {
        return ResponseEntity.ok(replyService.getReplyById(id));
    }

    @PostMapping
    public ResponseEntity<Void> postReply(@Valid @RequestBody NewReplyDto newReplyDto,
                                               @RequestParam Long ComentarioId) {
        String location =  replyService.postReply(newReplyDto, ComentarioId);
        URI locationHeader = URI.create(location);
        return ResponseEntity.created(locationHeader).build();
    }


    @PreAuthorize("hasRole('ROLE_PADRE')")
    @PatchMapping("/{id}")
    public ResponseEntity<Void> patchReply(@PathVariable Long id,@Valid @RequestBody NewReplyDto newReplyDto) throws AccessDeniedException {
        replyService.patchReply(id, newReplyDto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReplyById(@PathVariable Long id) throws AccessDeniedException {
        replyService.deleteReplyById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/comentario/{id}")
    public ResponseEntity<List<ReplySmallResponseDto>> getByComentarioId(@PathVariable Long id, @RequestParam int page, @RequestParam int size) {
        List<ReplySmallResponseDto> replysData = replyService.getByComentarioId(id, page, size);
        return ResponseEntity.ok(replysData);
    }
}
