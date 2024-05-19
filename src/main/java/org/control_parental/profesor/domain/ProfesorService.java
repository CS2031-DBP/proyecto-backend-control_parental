package org.control_parental.profesor.domain;

import org.control_parental.exceptions.ResourceAlreadyExistsException;
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
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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

    public void newProfesor(NewProfesorDto newProfesorDTO) {
        Profesor profesor = modelMapper.map(newProfesorDTO, Profesor.class);
        if(usuarioRepository.findByEmail(newProfesorDTO.getEmail()).isPresent()) {
            throw new ResourceAlreadyExistsException("El usuario ya existe");
        }
        profesor.setRole(Role.PROFESOR);
        profesor.setPassword(passwordEncoder.encode(newProfesorDTO.getPassword()));
        profesorRepository.save(profesor);
    }

    public ProfesorResponseDto getProfesorRepsonseDto(Long id) {
        Profesor profesor = profesorRepository.findById(id).orElseThrow();

        return modelMapper.map(profesor, ProfesorResponseDto.class);
    }

    public ProfesorSelfResponseDto getOwnProfesorInfo() {
        String email = "email@email.com";
        Profesor profesor = profesorRepository.findByEmail(email).orElseThrow();
        return modelMapper.map(profesor, ProfesorSelfResponseDto.class);
    }

    public void deleteProfesor(Long id) {
        profesorRepository.deleteById(id);
    }

    public List<Publicacion> getPublicaciones(Long id) {
        Profesor profesor = profesorRepository.findById(id).orElseThrow();
        return profesor.getPublicaciones();

    }

    public void patchPassword(NewPasswordDto newPasswordDto) {
         Profesor profesor = profesorRepository.findByEmail(newPasswordDto.getEmail()).orElseThrow();
         profesor.setPassword(newPasswordDto.getPassword());
         profesorRepository.save(profesor);

    }

    public void updateProfesor(Long id, NewProfesorDto newProfesorDto) {
        Profesor profesor = new Profesor();
        profesor.setEmail(newProfesorDto.getEmail());
        profesor.setNombre(newProfesorDto.getNombre());
        profesor.setApellido(newProfesorDto.getApellido());
        //profesor.setPassword(newProfesorDto.getPassword());
        //En teoria esto no se hace
        profesorRepository.save(profesor);
    }
}
