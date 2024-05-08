package org.control_parental.post.domain;

import org.control_parental.post.infrastructure.PostRepository;
import org.control_parental.salon.domain.Salon;
import org.control_parental.salon.infrastructure.SalonRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class PostService {
    @Autowired
    PostRepository repository;

    @Autowired
    SalonRepository salonRepository;


    @Autowired
    ModelMapper modelMapper;

    public List<PostRequestDTO> findPostsBySalonId(Long salon_id) {
        Salon salon = salonRepository.findById(salon_id).orElseThrow();
        List<Post> posts = repository.findAllBySalon(salon);

        List<PostRequestDTO> posts_data = new ArrayList<>();

        for(Post post : posts) {
            posts_data.add(modelMapper.map(post, PostRequestDTO.class));
        }

        return posts_data;
    }

    public void createPost(NewPostDTO newPostData, Long salon_id, List<Long> hijos_id) {
        Post post = modelMapper.map(newPostData, Post.class);
        Salon salon = salonRepository.findById(salon_id).orElseThrow();

        post.setFecha(LocalDateTime.now());
        post.setSalon(salon);


        repository.save(post);
    }
}
