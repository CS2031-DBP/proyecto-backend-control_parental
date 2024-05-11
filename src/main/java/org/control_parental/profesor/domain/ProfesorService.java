package org.control_parental.profesor.domain;

import org.control_parental.profesor.dto.NewProfesorDTO;
import org.control_parental.profesor.dto.ProfesorResponseDto;
import org.control_parental.profesor.dto.ProfesorSelfResponseDto;
import org.control_parental.profesor.infrastructure.ProfesorRepository;
import org.control_parental.publicacion.domain.Publicacion;
import org.control_parental.usuario.NewPasswordDto;
import org.control_parental.usuario.domain.Role;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProfesorService {

    @Autowired
    private ModelMapper modelMapper;
    private ProfesorRepository profesorRepository;

    public void newProfesor(NewProfesorDTO newProfesorDTO) {
        Profesor profesor = modelMapper.map(newProfesorDTO, Profesor.class);
        profesor.setRole(Role.PROFESOR);
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

}
