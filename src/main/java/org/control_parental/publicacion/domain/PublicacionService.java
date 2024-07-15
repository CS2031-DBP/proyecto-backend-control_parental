package org.control_parental.publicacion.domain;

import jakarta.transaction.Transactional;
import org.control_parental.configuration.AuthorizationUtils;
import org.control_parental.events.notification.NotificationEvent;
import org.control_parental.events.notification.ProfessorNotificationEvent;
import org.control_parental.events.uploadImage.UploadImageEvent;
import org.control_parental.exceptions.ResourceAlreadyExistsException;
import org.control_parental.exceptions.ResourceNotFoundException;
import org.control_parental.comentario.dto.ComentarioPublicacionDto;
import org.control_parental.hijo.domain.Hijo;
import org.control_parental.hijo.dto.HijoPublicacionDto;
import org.control_parental.hijo.infrastructure.HijoRepository;
import org.control_parental.like.Domain.Padre_Like;
import org.control_parental.like.Infrastructure.LikeRepository;
import org.control_parental.padre.domain.Padre;
import org.control_parental.padre.dto.PadrePublicacionDto;
import org.control_parental.padre.infrastructure.PadreRepository;
import org.control_parental.profesor.domain.Profesor;
import org.control_parental.profesor.infrastructure.ProfesorRepository;
import org.control_parental.publicacion.dto.NewPublicacionDto;
import org.control_parental.publicacion.dto.PublicacionResponseDto;
import org.control_parental.publicacion.infrastructure.PublicacionRepository;
import org.control_parental.s3.S3;
import org.control_parental.salon.domain.Salon;
import org.control_parental.salon.dto.ReducedSalonDto;
import org.control_parental.salon.infrastructure.SalonRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.AccessDeniedException;
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
    private ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    private S3 cliente;

    @Autowired
    HijoRepository hijoRepository;

    @Autowired
    private ProfesorRepository profesorRepository;

    @Autowired
    private AuthorizationUtils authorizationUtils;

    @Autowired
    private PadreRepository padreRepository;

    @Autowired
    private LikeRepository likeRepository;



    @Transactional
    public String savePublicacion(NewPublicacionDto newPublicacionDto, List<MultipartFile> fotos) throws IOException {
        //obtener quien lo esta publicando con Sprnig Scurity
        String email = authorizationUtils.authenticateUser();

        Profesor profesor = profesorRepository.findByEmail(email).orElseThrow(
                () -> new ResourceNotFoundException("El profesor no existe"));
        Publicacion newPublicacion = new Publicacion();
        Salon salon = salonRepository.findById(newPublicacionDto.getSalonId()).orElseThrow(
                ()-> new ResourceNotFoundException("El salon no existe"));

        List<Hijo> hijos = new ArrayList<>();
        newPublicacion.setTitulo(newPublicacionDto.getTitulo());
        newPublicacion.setDescripcion(newPublicacionDto.getDescripcion());
        newPublicacion.setUbicacion(newPublicacionDto.getUbicacion());
        newPublicacion.setLatitud(newPublicacionDto.getLatitud());
        newPublicacion.setLongitud(newPublicacionDto.getLongitud());
        newPublicacion.setFecha(LocalDateTime.now());
        newPublicacion.setProfesor(profesor);
        newPublicacion.setSalon(salon);
        newPublicacion.setFecha(LocalDateTime.now());
        newPublicacion.setLikes(0);

        newPublicacionDto.getHijos_id().forEach(hijo_id -> {
            Optional<Hijo> hijo = hijoRepository.findById(hijo_id);
            if (hijo.isPresent()) {
                hijos.add(hijo.get());
            }
        });
        newPublicacion.setHijos(hijos);

        profesor.getPublicaciones().add(newPublicacion);
        salon.getPublicaciones().add(newPublicacion);

        publicacionRepository.save(newPublicacion);
        profesorRepository.save(profesor);

        // Conversion de MultipartFile a File aqui y no en el listener porque en el listener moria, botaba un
        // java.nio.NoSuchFileException, yo presumo porque debe de tratarse de algun path que se muere en el camino.
        // pero no estoy seguro la verdad

        List<File> files = new ArrayList<>();
        fotos.forEach(foto -> {
            final File file;
            try {
                file = cliente.convertMultiPartFileToFile(foto);
                files.add(file);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        applicationEventPublisher.publishEvent(
                new UploadImageEvent(newPublicacion.getId(), files)
        );

        applicationEventPublisher.publishEvent(
                new NotificationEvent(
                        newPublicacion.getSalon(),
                        "¡Nueva Publicación en el Salon " + newPublicacion.getSalon().getNombre(),
                        newPublicacion.getTitulo())
        );

        applicationEventPublisher.publishEvent(
                new ProfessorNotificationEvent(
                        "¡Publicación creada de forma exitosa!",
                        newPublicacion.getTitulo(),
                        profesor.getNotificationToken())
        );

        return "/" + newPublicacion.getId();
    }

    public PublicacionResponseDto getPublicacionById(Long id) {
        Publicacion publicacion = publicacionRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Esta publicacion no existe"));
        PublicacionResponseDto publicacionResponseDto = modelMapper.map(publicacion, PublicacionResponseDto.class);

        List<HijoPublicacionDto> hijoPublicacionDtos = new ArrayList<>();
        publicacion.getHijos().forEach((hijo) -> {
            hijoPublicacionDtos.add(modelMapper.map(hijo, HijoPublicacionDto.class));
        });

        List<ComentarioPublicacionDto> comentarioPublicacionDtos = new ArrayList<>();
        publicacion.getComentarios().forEach((comentario) -> {
            comentarioPublicacionDtos.add(modelMapper.map(comentario, ComentarioPublicacionDto.class));
        });

        List<PadrePublicacionDto> padrePublicacionDtos = new ArrayList<>();
        publicacion.getLikers().forEach((liker) -> {
            padrePublicacionDtos.add(modelMapper.map(liker.getPadre(), PadrePublicacionDto.class));
        });

        publicacionResponseDto.setHijos(hijoPublicacionDtos);
        publicacionResponseDto.setComentarios(comentarioPublicacionDtos);
        publicacionResponseDto.setLikers(padrePublicacionDtos);
        publicacionResponseDto.setSalon(modelMapper.map(publicacion.getSalon(), ReducedSalonDto.class));
        return publicacionResponseDto;
    }

    public void deletePublicacion(Long id) throws AccessDeniedException {
        String email = authorizationUtils.authenticateUser();
        Long userId = publicacionRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Publicación no encontrada")).getProfesor().getId();
        authorizationUtils.verifyUserAuthorization(email, userId);

        Profesor profesor = profesorRepository.findById(userId).orElseThrow(()-> new ResourceNotFoundException("Profesor no encontrado"));
        Publicacion publicacion = publicacionRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Publicacion no encontrada"));
        Salon salon = salonRepository.findById(publicacion.getSalon().getId()).orElseThrow(()-> new ResourceNotFoundException("Salon no encontrado"));

        salon.removePublicacion(publicacion);
        profesor.removePublicacion(publicacion);
        salonRepository.save(salon);
        profesorRepository.save(profesor);
        publicacionRepository.deleteById(id);
    }

    /*public void patchPublicacion(Long id, NewPublicacionDto newPublicacionDto){
        Publicacion publicacion = publicacionRepository.findById(id).orElseThrow();
        publicacion.setFoto(newPublicacionDto.getFoto());
        publicacion.setTitulo(newPublicacionDto.getTitulo());
        publicacion.setDescripcion(newPublicacionDto.getDescripcion());
        List<Hijo> hijos = new ArrayList<Hijo>();
        newPublicacionDto.getHijos_id().forEach(hijoId -> {hijos.add(hijoRepository.findById(hijoId).orElseThrow());});
        publicacion.setHijos(hijos);
    }*/

    public List<PublicacionResponseDto> findPostsForPadre(int page, int size) {
        String email = authorizationUtils.authenticateUser();

        Padre padre = padreRepository.findByEmail(email).orElseThrow(()-> new ResourceNotFoundException("No se encontro al padre"));

        List<Hijo> hijos = padre.getHijos();

        List<Salon> salones = new ArrayList<>();

        hijos.forEach(hijo ->
                salones.add(hijo.getSalon()));

        Pageable pageable = PageRequest.of(page, size);

        profesorRepository.findAll();

        Page<Publicacion> publicaciones = publicacionRepository.findAllBySalonInOrderByFechaDesc(salones, pageable);

        List<PublicacionResponseDto> response = new ArrayList<>();
        publicaciones.forEach(publicacion -> {
            PublicacionResponseDto res = modelMapper.map(publicacion, PublicacionResponseDto.class);
            response.add(res);
        });

        return response;
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
        Publicacion publicacion = publicacionRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Publicación no encontrada"));
        publicacion.setLikes(publicacion.getLikes() - 1);
        publicacionRepository.save(publicacion);
        likeRepository.delete(like);
    }

    public List<PublicacionResponseDto> findPostsBySalon(Long id, int page, int size) {
        Salon salon = salonRepository.findById(id).orElseThrow();
        List<Salon> salonList = new ArrayList<>();
        salonList.add(salon);
        Pageable pageable = PageRequest.of(page, size);
        Page<Publicacion> publicacionesPage = publicacionRepository.findAllBySalonInOrderByFechaDesc(salonList, pageable);
        List<PublicacionResponseDto> publicacionesData = new ArrayList<>();

        for(Publicacion i : publicacionesPage) {
            publicacionesData.add(modelMapper.map(i, PublicacionResponseDto.class));
        }

        return publicacionesData;

    }

    public List<PublicacionResponseDto> findOwnPostsBySalon(Long id, int page, int size) {
        String email = authorizationUtils.authenticateUser();
        Salon salon = salonRepository.findById(id).orElseThrow();
        Profesor profesor = profesorRepository.findByEmail(email).orElseThrow();
        Pageable pageable = PageRequest.of(page, size);
        Page<Publicacion> publicacionesPage = publicacionRepository.findAllBySalonAndProfesorOrderByFechaDesc(salon, profesor, pageable);
        List<PublicacionResponseDto> publicacionesData = new ArrayList<>();

        for(Publicacion i : publicacionesPage) {
            publicacionesData.add(modelMapper.map(i, PublicacionResponseDto.class));
        }

        return publicacionesData;
    }
}
