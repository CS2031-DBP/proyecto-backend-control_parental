package org.control_parental.profesor.domain;

import org.control_parental.admin.domain.Admin;
import org.control_parental.admin.infrastructure.AdminRepository;
import org.control_parental.configuration.AuthorizationUtils;
import org.control_parental.csv.CSVHelper;
import org.control_parental.email.nuevaContraseña.NuevaContaseñaEmailEvent;
import org.control_parental.email.nuevoUsuario.NuevoUsuarioEmailEvent;
import org.control_parental.exceptions.ResourceAlreadyExistsException;
import org.control_parental.exceptions.ResourceNotFoundException;
import org.control_parental.profesor.dto.NewProfesorDto;
import org.control_parental.profesor.dto.ProfesorResponseDto;
import org.control_parental.profesor.dto.ProfesorSelfResponseDto;
import org.control_parental.profesor.infrastructure.ProfesorRepository;
import org.control_parental.publicacion.domain.Publicacion;
import org.control_parental.usuario.NewPasswordDto;
import org.control_parental.usuario.domain.Role;
import org.control_parental.usuario.domain.Usuario;
import org.control_parental.usuario.infrastructure.UsuarioRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ProfesorService {

    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private ProfesorRepository profesorRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UsuarioRepository<Usuario> usuarioRepository;

    @Autowired
    ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    AuthorizationUtils authorizationUtils;

    @Autowired
    AdminRepository adminRepository;


    public String newProfesor(NewProfesorDto newProfesorDTO) {
        String email = authorizationUtils.authenticateUser();
        Admin admin = adminRepository.findByEmail(email).orElseThrow(()-> new ResourceNotFoundException("No se encontro al admin"));

        Profesor profesor = modelMapper.map(newProfesorDTO, Profesor.class);

        //if(newProfesorDTO.getEmail().equals("jorgerios@utec.edu.pe")) throw new ResourceAlreadyExistsException("El usuario ya existe");
        if(usuarioRepository.findByEmail(newProfesorDTO.getEmail()).isPresent()) {
            throw new ResourceAlreadyExistsException("El usuario ya existe");
        }
        profesor.setNido(admin.getNido());
        profesor.setRole(Role.PROFESOR);
        profesor.setPassword(passwordEncoder.encode(newProfesorDTO.getPassword()));
        applicationEventPublisher.publishEvent(
                new NuevoUsuarioEmailEvent(this, profesor.getEmail(),
                        newProfesorDTO.getPassword(),
                        profesor.getNombre(),
                        profesor.getRole().toString())
        );

        profesorRepository.save(profesor);

        return "/"+profesor.getId();
    }

    public ProfesorResponseDto getProfesorRepsonseDto(Long id) {
        Profesor profesor = profesorRepository.findById(id).orElseThrow();

        return modelMapper.map(profesor, ProfesorResponseDto.class);
    }

    public ProfesorSelfResponseDto getOwnProfesorInfo() {
        String email = authorizationUtils.authenticateUser();
        Profesor profesor = profesorRepository.findByEmail(email).orElseThrow();
        return modelMapper.map(profesor, ProfesorSelfResponseDto.class);
    }

    public void deleteProfesor(Long id) throws AccessDeniedException {
        //String email = authorizationUtils.authenticateUser();
        //authorizationUtils.verifyUserAuthorization(email, id);
        profesorRepository.deleteById(id);
    }

    public List<Publicacion> getPublicaciones(Long id) {
        Profesor profesor = profesorRepository.findById(id).orElseThrow();
        return profesor.getPublicaciones();

    }

    public void patchPassword(NewPasswordDto newPasswordDto) {
        String email = authorizationUtils.authenticateUser();
         Profesor profesor = profesorRepository.findByEmail(email).orElseThrow(()-> new ResourceNotFoundException("El profesor no fue encontrado"));
         Date hora = new Date();
         applicationEventPublisher.publishEvent(
                 new NuevaContaseñaEmailEvent(profesor.getNombre(), profesor.getEmail(), hora)
         );
         profesor.setPassword(passwordEncoder.encode(newPasswordDto.getPassword()));
         profesorRepository.save(profesor);

    }

    public void updateProfesor(Long id, NewProfesorDto newProfesorDto) {

        Profesor profesor = profesorRepository.findById(id).orElseThrow(
                ()-> new ResourceNotFoundException("El profesor no fue encontrado"));
        profesor.setEmail(newProfesorDto.getEmail());
        profesor.setNombre(newProfesorDto.getNombre());
        profesor.setApellido(newProfesorDto.getApellido());
        //profesor.setPassword(newProfesorDto.getPassword());
        //En teoria esto no se hace
        profesorRepository.save(profesor);
    }

    public void saveCsvProfesores(MultipartFile file) throws IOException {
        List<NewProfesorDto> profesores = CSVHelper.csvToProfesor(file.getInputStream());
        List<Profesor> newProfesores = new ArrayList<>();
        profesores.forEach(profesor -> {
            Profesor nuevoProfesor = modelMapper.map(profesor, Profesor.class);
            nuevoProfesor.setRole(Role.PROFESOR);
            nuevoProfesor.setPassword(passwordEncoder.encode(profesor.getPassword()));
            newProfesores.add(nuevoProfesor);
        });
        profesorRepository.saveAll(newProfesores);
    }

}
