package org.control_parental.publicacion.domain;

import org.control_parental.publicacion.dto.NewPublicacionDTO;
import org.control_parental.publicacion.dto.PostRequestDTO;
import org.control_parental.publicacion.infrastructure.PublicacionRepository;
import org.control_parental.salon.domain.Salon;
import org.control_parental.salon.infrastructure.SalonRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class PublicacionService {
    @Autowired
    PublicacionRepository repository;

    @Autowired
    SalonRepository salonRepository;


    @Autowired
    ModelMapper modelMapper;

    public List<PostRequestDTO> findPostsBySalonId(Long salon_id) {
        Salon salon = salonRepository.findById(salon_id).orElseThrow();
        List<Publicacion> publicaciones = repository.findAllBySalon(salon);

        List<PostRequestDTO> posts_data = new ArrayList<>();

        for(Publicacion publicacion : publicaciones) {
            posts_data.add(modelMapper.map(publicacion, PostRequestDTO.class));
        }

        return posts_data;
    }

    public void createPost(NewPublicacionDTO newPostData, Long salon_id, List<Long> hijos_id) {
        Publicacion publicacion = modelMapper.map(newPostData, Publicacion.class);
        Salon salon = salonRepository.findById(salon_id).orElseThrow();

        publicacion.setFecha(LocalDateTime.now());
        publicacion.setSalon(salon);


        repository.save(publicacion);
    }
}
