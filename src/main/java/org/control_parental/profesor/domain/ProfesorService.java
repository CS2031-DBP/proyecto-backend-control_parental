package org.control_parental.profesor.domain;

import org.control_parental.admin.domain.Admin;
import org.control_parental.admin.infrastructure.AdminRepository;
import org.control_parental.configuration.AuthorizationUtils;
import org.control_parental.configuration.RandomCode;
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
import org.control_parental.salon.domain.Salon;
import org.control_parental.salon.infrastructure.SalonRepository;
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

    @Autowired
    SalonRepository salonRepository;


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

        RandomCode rc = new RandomCode();
        String password = rc.generatePassword();
        profesor.setPassword(passwordEncoder.encode(password));
        applicationEventPublisher.publishEvent(
                new NuevoUsuarioEmailEvent(this, profesor.getEmail(),
                        password,
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
                 new NuevaContaseñaEmailEvent(profesor.getNombre(), profesor.getEmail(), hora, newPasswordDto.getPassword())
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
        String email = authorizationUtils.authenticateUser();
        Admin admin = adminRepository.findByEmail(email).orElseThrow(()-> new ResourceNotFoundException("el admin no fue encontrado"));

        List<NewProfesorDto> profesores = CSVHelper.csvToProfesor(file.getInputStream());

        List<Profesor> newProfesores = new ArrayList<>();
        profesores.forEach(profesor -> {
            Profesor nuevoProfesor = modelMapper.map(profesor, Profesor.class);
            nuevoProfesor.setRole(Role.PROFESOR);
            RandomCode rc = new RandomCode();
            String password = rc.generatePassword();
            nuevoProfesor.setPassword(passwordEncoder.encode(password));
            nuevoProfesor.setNido(admin.getNido());
            newProfesores.add(nuevoProfesor);
            applicationEventPublisher.publishEvent(
                    new NuevoUsuarioEmailEvent(this,
                            nuevoProfesor.getEmail(),
                            password,
                            nuevoProfesor.getNombre(),
                            nuevoProfesor.getRole().toString())
            );
        });
        profesorRepository.saveAll(newProfesores);
    }

    public List<ProfesorResponseDto> getProfessorBySalon(Long SalonId) {
        String email = authorizationUtils.authenticateUser();
        Salon salon = salonRepository.findById(SalonId).orElseThrow(()-> new ResourceNotFoundException("No existe este salon"));

        List<Profesor> profesores = profesorRepository.findAllBySalones(salon);
        List<ProfesorResponseDto> responseDtos = new ArrayList<>();
        profesores.forEach(profesor -> {
            ProfesorResponseDto prof = modelMapper.map(profesor, ProfesorResponseDto.class);
            responseDtos.add(prof);
        });

        return responseDtos;

    }

    public List<ProfesorResponseDto> getAllProfesores(int page, int size){
        String email = authorizationUtils.authenticateUser();
        Pageable pageable = PageRequest.of(page, size);
        Page<Profesor> profesoresPage = profesorRepository.findAll(pageable);
        profesorRepository.count();

        List<ProfesorResponseDto> response = new ArrayList<>();
        profesoresPage.forEach(profesor -> {
            ProfesorResponseDto res = modelMapper.map(profesor, ProfesorResponseDto.class);
            response.add(res);
        });
        return response;
    }

}
