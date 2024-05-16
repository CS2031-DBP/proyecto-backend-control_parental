package org.control_parental.publicacion.domain;

import jakarta.persistence.EntityNotFoundException;
import org.control_parental.comentario.dto.ComentarioPublicacionDto;
import org.control_parental.hijo.domain.Hijo;
import org.control_parental.hijo.dto.HijoPublicacionDto;
import org.control_parental.hijo.infrastructure.HijoRepository;
import org.control_parental.profesor.domain.Profesor;
import org.control_parental.profesor.dto.ProfesorPublicacionDto;
import org.control_parental.profesor.dto.ProfesorResponseDto;
import org.control_parental.profesor.infrastructure.ProfesorRepository;
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
    @Autowired
    private ProfesorRepository profesorRepository;

    public void savePublicacion(NewPublicacionDto newPublicacionDto) {
        //obtener quien lo esta publicando con Sprnig Scurity
        Long ProfesorId = 1L;
        Profesor profesor = profesorRepository.findById(ProfesorId).orElseThrow(EntityNotFoundException::new);
        Publicacion newPublicacion = new Publicacion();
        Salon salon = salonRepository.findById(newPublicacionDto.getSalonId()).orElseThrow(EntityNotFoundException::new);
        List<Hijo> hijos = new ArrayList<Hijo>();

        newPublicacion.setTitulo(newPublicacionDto.getTitulo());
        newPublicacion.setDescripcion(newPublicacionDto.getDescripcion());
        newPublicacion.setFecha(LocalDateTime.now());
        newPublicacion.setFoto(newPublicacionDto.getFoto());
        newPublicacion.setProfesor(profesor);
        newPublicacion.setSalon(salon);
        newPublicacion.setFecha(LocalDateTime.now());
        newPublicacion.setLikes(0);

        newPublicacionDto.getHijos_id().forEach((hijo_id) -> {
            hijos.add(hijoRepository.findById(hijo_id).orElseThrow(EntityNotFoundException::new));
        });
        newPublicacion.setHijos(hijos);

        profesor.getPublicaciones().add(newPublicacion);
        salon.getPublicaciones().add(newPublicacion);
        publicacionRepository.save(newPublicacion);
    }

    public PublicacionResponseDto getPublicacionById(Long id) {
        Publicacion publicacion = publicacionRepository.findById(id).orElseThrow();
        PublicacionResponseDto publicacionResponseDto = modelMapper.map(publicacion, PublicacionResponseDto.class);
        publicacionResponseDto.setProfesor(modelMapper.map(publicacion.getProfesor(), ProfesorPublicacionDto.class));

        List<HijoPublicacionDto> hijoPublicacionDtos = new ArrayList<>();
        publicacion.getHijos().forEach((hijo) -> {
            hijoPublicacionDtos.add(modelMapper.map(hijo, HijoPublicacionDto.class));
        });

        List<ComentarioPublicacionDto> comentarioPublicacionDtos = new ArrayList<>();
        publicacion.getComentarios().forEach((comentario) -> {
            comentarioPublicacionDtos.add(modelMapper.map(comentario, ComentarioPublicacionDto.class));
        });

        publicacionResponseDto.setHijos(hijoPublicacionDtos);
        publicacionResponseDto.setComentarios(comentarioPublicacionDtos);
        return publicacionResponseDto;
    }

    public void deletePublicacion(Long id) {
        publicacionRepository.deleteById(id);
    }
/*
    public void patchPublicacion(Long id, NewPublicacionDto newPublicacionDto){
        Publicacion publicacion = publicacionRepository.findById(id).orElseThrow();
        publicacion.setFoto(newPublicacionDto.getFoto());
        publicacion.setTitulo(newPublicacionDto.getTitulo());
        publicacion.setDescripcion(newPublicacionDto.getDescripcion());
        List<Hijo> hijos = new ArrayList<Hijo>();
        newPublicacionDto.getHijos_id().forEach(hijoId -> {hijos.add(hijoRepository.findById(hijoId).orElseThrow());});
        publicacion.setHijos(hijos);
    }
*/
    public List<PublicacionResponseDto> findPostsBySalonId(Long salon_id) {
        Salon salon = salonRepository.findById(salon_id).orElseThrow();
        List<Publicacion> publicaciones = publicacionRepository.findAllBySalon(salon);

        List<PublicacionResponseDto> posts_data = new ArrayList<>();

        for(Publicacion publicacion : publicaciones) {
            posts_data.add(modelMapper.map(publicacion, PublicacionResponseDto.class));
        }

        return posts_data;
    }
}
