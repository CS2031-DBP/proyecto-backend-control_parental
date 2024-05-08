package org.control_parental.post.application;

import org.control_parental.post.domain.NewPostDTO;
import org.control_parental.post.domain.PostRequestDTO;
import org.control_parental.post.domain.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("/posts")
public class PostController {

    @Autowired
    PostService service;

    @GetMapping("/{salon_id}")
    public ResponseEntity<List<PostRequestDTO>> findPostsBySalonId(@PathVariable Long salon_id) {
        return ResponseEntity.ok(service.findPostsBySalonId(salon_id));
    }

    @PostMapping("/{salon_id}")
    public ResponseEntity<Void> createPost(@PathVariable Long salon_id, @RequestParam List<Long> hijos_id, @RequestBody NewPostDTO newPostData) {
        service.createPost(newPostData, salon_id, hijos_id);
        return ResponseEntity.created(null).build();
    }

}
