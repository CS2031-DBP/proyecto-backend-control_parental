package org.control_parental.publicacion.domain;

import jakarta.persistence.EntityNotFoundException;
import org.control_parental.hijo.domain.Hijo;
import org.control_parental.hijo.infrastructure.HijoRepository;
import org.control_parental.publicacion.dto.NewPublicacionDto;
import org.control_parental.publicacion.dto.PublicacionResponseDto;
import org.control_parental.publicacion.infrastructure.PublicacionRepository;
import org.control_parental.salon.domain.Salon;
import org.control_parental.salon.infrastructure.SalonRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class PublicacionService {
    @Autowired
    PublicacionRepository publicacionRepository;

    @Autowired
    SalonRepository salonRepository;

    @Autowired
    ModelMapper modelMapper;
    @Autowired
    private HijoRepository hijoRepository;

    public void savePublicacion(NewPublicacionDto newPublicacionDto) {
        publicacionRepository.save(modelMapper.map(newPublicacionDto, Publicacion.class));
    }

    public PublicacionResponseDto getPublicacionById(Long id) {
        Publicacion publicacion = publicacionRepository.findById(id).orElseThrow();
        return modelMapper.map(publicacion, PublicacionResponseDto.class);
    }

    public void deletePublicacion(Long id) {
        publicacionRepository.deleteById(id);
    }

    public void patchPublicacion(Long id, NewPublicacionDto newPublicacionDto){
        Publicacion publicacion = publicacionRepository.findById(id).orElseThrow();
        publicacion.setFoto(newPublicacionDto.getFoto());
        publicacion.setTitulo(newPublicacionDto.getTitulo());
        publicacion.setDescripcion(newPublicacionDto.getDescripcion());
        List<Hijo> hijos = new ArrayList<Hijo>();
        newPublicacionDto.getHijos_id().forEach(hijoId -> {hijos.add(hijoRepository.findById(hijoId).orElseThrow());});
        publicacion.setHijos(hijos);
    }

    public List<PublicacionResponseDto> findPostsBySalonId(Long salon_id) {
        Salon salon = salonRepository.findById(salon_id).orElseThrow();
        List<Publicacion> publicaciones = publicacionRepository.findAllBySalon(salon);

        List<PublicacionResponseDto> posts_data = new ArrayList<>();

        for(Publicacion publicacion : publicaciones) {
            posts_data.add(modelMapper.map(publicacion, PublicacionResponseDto.class));
        }

        return posts_data;
    }

    public void createPost(NewPublicacionDto newPostData, Long salon_id, List<Long> hijos_id) {
        Publicacion publicacion = modelMapper.map(newPostData, Publicacion.class);
        Salon salon = salonRepository.findById(salon_id).orElseThrow();

        publicacion.setFecha(LocalDateTime.now());
        publicacion.setSalon(salon);


        publicacionRepository.save(publicacion);
    }
}
