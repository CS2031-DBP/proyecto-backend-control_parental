package org.control_parental.padre.domain;

import org.control_parental.admin.domain.Admin;
import org.control_parental.admin.infrastructure.AdminRepository;
import org.control_parental.auth.AuthorizationUtils;
import org.control_parental.configuration.RandomCode;
import org.control_parental.csv.CSVHelper;
import org.control_parental.email.nuevaContraseña.NuevaContaseñaEmailEvent;
import org.control_parental.email.nuevoUsuario.NuevoUsuarioEmailEvent;
import org.control_parental.exceptions.ResourceAlreadyExistsException;
import org.control_parental.exceptions.ResourceNotFoundException;
import org.control_parental.hijo.domain.Hijo;
import org.control_parental.like.Domain.Padre_Like;
import org.control_parental.like.Infrastructure.LikeRepository;
import org.control_parental.padre.dto.NewPadreDto;
import org.control_parental.padre.dto.PadreResponseDto;
import org.control_parental.padre.dto.PadreSelfResponseDto;
import org.control_parental.padre.infrastructure.PadreRepository;
import org.control_parental.publicacion.dto.PublicacionResponseDto;
import org.control_parental.usuario.NewPasswordDto;
import org.control_parental.usuario.domain.Role;
import org.control_parental.usuario.domain.Usuario;
import org.control_parental.usuario.infrastructure.UsuarioRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class PadreService {
    @Autowired
    PadreRepository padreRepository;

    @Autowired
    AdminRepository adminRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UsuarioRepository<Usuario> usuarioRepository;

    @Autowired
    private LikeRepository likeRepository;

    @Autowired
    ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    AuthorizationUtils authorizationUtils;

    public String savePadre(NewPadreDto newPadreDto) {
        String email = authorizationUtils.authenticateUser();

        Admin admin = adminRepository.findByEmail(email).orElseThrow(()-> new ResourceAlreadyExistsException("No se encontro al administrador"));

        Padre padre = modelMapper.map(newPadreDto, Padre.class);
        if(usuarioRepository.findByEmail(newPadreDto.getEmail()).isPresent()) {
            throw new ResourceAlreadyExistsException("el usuario ya existe");
        }

        RandomCode rc = new RandomCode();
        String password = rc.generatePassword();
        padre.setPassword(passwordEncoder.encode(password));
        padre.setRole(Role.PADRE);
        padre.setNido(admin.getNido());

        applicationEventPublisher.publishEvent(
                new NuevoUsuarioEmailEvent(this, padre.getEmail(),
                        password,
                        padre.getNombre(),
                        padre.getRole().toString())
        );
        padreRepository.save(padre);
        return "/" + padre.getId();
    }

    public void savePadresCsv(MultipartFile file) throws IOException {
        String email = authorizationUtils.authenticateUser();
        Admin admin = adminRepository.findByEmail(email).orElseThrow(()-> new ResourceNotFoundException("Admin no encontrado"));

        List<NewPadreDto> padres = CSVHelper.csvToPadres(file.getInputStream());
        List<Padre> newPadres = new ArrayList<>();
        padres.forEach(padre -> {
            Padre nuevoPadre = modelMapper.map(padre, Padre.class);
            nuevoPadre.setRole(Role.PADRE);
            RandomCode rc = new RandomCode();
            String password = rc.generatePassword();
            nuevoPadre.setPassword(passwordEncoder.encode(password));
            nuevoPadre.setNido(admin.getNido());
            newPadres.add(nuevoPadre);
            applicationEventPublisher.publishEvent(
                    new NuevoUsuarioEmailEvent(this,
                            nuevoPadre.getEmail(),
                            password,
                            nuevoPadre.getNombre(),
                            nuevoPadre.getRole().toString())
            );
        }
        );

        padreRepository.saveAll(newPadres);
    }

    public PadreResponseDto getPadreById(Long id) {
        Padre padre  = padreRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("El padre no fue encontrado"));

        return modelMapper.map(padre, PadreResponseDto.class);
    }

    public PadreSelfResponseDto getOwnInfo() {
        String email = authorizationUtils.authenticateUser();
        Padre padre = padreRepository.findByEmail(email).orElseThrow(
                ()-> new ResourceNotFoundException("Padre no fue encontrado")
        );

        PadreSelfResponseDto response = modelMapper.map(padre, PadreSelfResponseDto.class);

        List<PublicacionResponseDto> publicacionResponseDtos = new ArrayList<>();
        padre.getPosts_likeados().forEach(post -> {
            publicacionResponseDtos.add(modelMapper.map(post.getPublicacion(), PublicacionResponseDto.class));
        });

        response.setPublicaciones_likeadas(publicacionResponseDtos);
        return response;
    }

    public void deletePadre(Long id) throws AccessDeniedException {
        //String userEmail = authorizationUtils.authenticateUser();
        //authorizationUtils.verifyUserAuthorization(userEmail, id);
        padreRepository.deleteById(id);
    }

    public List<Hijo> getHijos(Long id) {
        Padre padre = padreRepository.findById(id).orElseThrow();
        return padre.getHijos();
    }

    public List<Hijo> getOwnHijos() {
        String email = authorizationUtils.authenticateUser();
        Padre padre = padreRepository.findByEmail(email).orElseThrow(
                ()-> new ResourceNotFoundException("Padre no fue encontrado")
        );
        return padre.getHijos();
    }

    public void newPassword(NewPasswordDto newPasswordDto) throws AccessDeniedException {
        String email = authorizationUtils.authenticateUser();
        if (!Objects.equals(email, newPasswordDto.getEmail()))
            throw new AccessDeniedException("Usuario no authorizado para cambiar esta contraseña");
        Padre padre = padreRepository.findByEmail(email).orElseThrow(
                ()-> new ResourceNotFoundException("Padre no encontrado"));
        padre.setPassword(passwordEncoder.encode(newPasswordDto.getPassword()));
        Date date = new Date();
        applicationEventPublisher.publishEvent(
                new NuevaContaseñaEmailEvent(padre.getNombre(), padre.getEmail(), date, newPasswordDto.getPassword())
        );
        padreRepository.save(padre);
    }

    public List<PadreResponseDto> getAllPadres(int page, int size) {
        String email = authorizationUtils.authenticateUser();

        Pageable pageable = PageRequest.of(page, size);

        Page<Padre> padres = padreRepository.findAllBy(pageable);
        List<PadreResponseDto> responseDtos = new ArrayList<>();
        padres.forEach(padre -> {responseDtos.add(modelMapper.map(padre, PadreResponseDto.class));});

        return responseDtos;
    }

    public List<Long> getLiked() {
        String email = authorizationUtils.authenticateUser();

        Long id = padreRepository.findByEmail(email).orElseThrow().getId();

        List<Padre_Like> likes = likeRepository.findAllByPadre_Id(id);

        List<Long> ids = new ArrayList<>();

        for(Padre_Like i : likes) {
            ids.add(i.getPublicacion().getId());
        }

        return ids;
    }
}