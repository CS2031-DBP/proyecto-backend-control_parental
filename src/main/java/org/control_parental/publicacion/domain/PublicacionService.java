package org.control_parental.publicacion.domain;

import org.control_parental.configuration.AuthorizationUtils;
import org.control_parental.email.nuevaPublicacion.PublicacionEmailEvent;
import org.control_parental.exceptions.ResourceAlreadyExistsException;
import org.control_parental.exceptions.ResourceNotFoundException;
import org.control_parental.comentario.dto.ComentarioPublicacionDto;
import org.control_parental.hijo.domain.Hijo;
import org.control_parental.hijo.dto.HijoPublicacionDto;
import org.control_parental.hijo.infrastructure.HijoRepository;
import org.control_parental.like.Domain.Padre_Like;
import org.control_parental.like.Infrastructure.LikeRepository;
import org.control_parental.padre.domain.Padre;
import org.control_parental.padre.infrastructure.PadreRepository;
import org.control_parental.profesor.domain.Profesor;
import org.control_parental.profesor.dto.ProfesorResponseDto;
import org.control_parental.profesor.infrastructure.ProfesorRepository;
import org.control_parental.publicacion.dto.NewPublicacionDto;
import org.control_parental.publicacion.dto.PublicacionResponseDto;
import org.control_parental.publicacion.infrastructure.PublicacionRepository;
import org.control_parental.salon.domain.Salon;
import org.control_parental.salon.infrastructure.SalonRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PublicacionService {
    @Autowired
    PublicacionRepository publicacionRepository;

    @Autowired
    SalonRepository salonRepository;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    HijoRepository hijoRepository;

    @Autowired
    private ProfesorRepository profesorRepository;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    private AuthorizationUtils authorizationUtils;

    @Autowired
    private PadreRepository padreRepository;

    @Autowired
    private LikeRepository likeRepository;

//    public void savePublicacion(NewPublicacionDto newPublicacionDto) {
//        Publicacion newPublicacion = modelMapper.map(newPublicacionDto, Publicacion.class);
//        List<Long> hijosid = newPublicacionDto.getHijos_id();
//        hijosid.forEach(id -> {
//            Hijo hijo = hijoRepository.findById(id).orElseThrow(
//                    () -> new ResourceNotFoundException("El niño no existe"));
//            newPublicacion.addStudent(hijo);
//        });
//        newPublicacion.setFecha(LocalDateTime.now());
//        newPublicacion.setLikes(0);

    public void savePublicacion(NewPublicacionDto newPublicacionDto) {
        //obtener quien lo esta publicando con Sprnig Scurity
        String email = authorizationUtils.authenticateUser();

        Profesor profesor = profesorRepository.findByEmail(email).orElseThrow(
                () -> new ResourceNotFoundException("El profesor no existe"));
        Publicacion newPublicacion = new Publicacion();
        Salon salon = salonRepository.findById(newPublicacionDto.getSalonId()).orElseThrow(
                ()-> new ResourceNotFoundException("El salon no existe"));

        List<Hijo> hijos = new ArrayList<Hijo>();
        newPublicacion.setTitulo(newPublicacionDto.getTitulo());
        newPublicacion.setDescripcion(newPublicacionDto.getDescripcion());
        newPublicacion.setFecha(LocalDateTime.now());
        newPublicacion.setFoto(newPublicacionDto.getFoto());
        newPublicacion.setProfesor(profesor);
        newPublicacion.setSalon(salon);
        newPublicacion.setFecha(LocalDateTime.now());
        newPublicacion.setLikes(0);

        newPublicacionDto.getHijos_id().forEach(hijo_id -> {
            Optional<Hijo> hijo = hijoRepository.findById(hijo_id);
            if (hijo.isPresent()) {
                hijos.add(hijo.get());
                applicationEventPublisher.publishEvent(
                        new PublicacionEmailEvent(this,
                                hijo.get().getNombre(),
                                hijo.get().getPadre().getEmail(),
                                newPublicacion.getTitulo(),
                                hijo.get().getPadre().getNombre())
                );
            }
        });
        newPublicacion.setHijos(hijos);

        profesor.getPublicaciones().add(newPublicacion);
        salon.getPublicaciones().add(newPublicacion);
        publicacionRepository.save(newPublicacion);
    }

    public PublicacionResponseDto getPublicacionById(Long id) {
        Publicacion publicacion = publicacionRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Esta publicacion no existe"));
        PublicacionResponseDto publicacionResponseDto = modelMapper.map(publicacion, PublicacionResponseDto.class);
        publicacionResponseDto.setProfesor(modelMapper.map(publicacion.getProfesor(), ProfesorResponseDto.class));

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

    public void deletePublicacion(Long id)
    {
        String email = authorizationUtils.authenticateUser();
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

    public void likePost(Long postId) {
        String email = authorizationUtils.authenticateUser();
        Publicacion publicacion = publicacionRepository.findById(postId).orElseThrow(
                () -> new ResourceNotFoundException("Esta publicacion no fue encontrada"));
        Padre padre = padreRepository.findByEmail(email).orElseThrow(
                () -> new ResourceNotFoundException("Padre no encontrado"));
        Optional<Padre_Like> like = likeRepository.findByPadre_IdAndPublicacion_Id(padre.getId(), postId);
        if (like.isPresent()) {
            throw new ResourceAlreadyExistsException("Usted ya le dio like a este post!");
        }


        Padre_Like nuevoLike = new Padre_Like();
        nuevoLike.setPadre(padre);
        nuevoLike.setPublicacion(publicacion);

        publicacion.addLike(nuevoLike);

        publicacionRepository.save(publicacion);
        likeRepository.save(nuevoLike);
    }

    public void deLikePost(Long postId) {
        String email = authorizationUtils.authenticateUser();
        Padre padre = padreRepository.findByEmail(email).orElseThrow(
                ()-> new ResourceNotFoundException("Padre no encontrado")
        );
        Padre_Like like = likeRepository.findByPadre_IdAndPublicacion_Id(padre.getId(), postId).orElseThrow(
                ()-> new ResourceNotFoundException("Esta persona no ha dado like a esta publicacion")
        );
        likeRepository.delete(like);
    }
}
