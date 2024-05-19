package org.control_parental.salon.domain;

import org.control_parental.exceptions.ResourceAlreadyExistsExpeption;
import org.control_parental.exceptions.ResourceNotFoundException;
import org.control_parental.hijo.domain.Hijo;

import org.control_parental.hijo.dto.ReducedHijoDto;
import org.control_parental.hijo.infrastructure.HijoRepository;
import org.control_parental.profesor.domain.Profesor;
import org.control_parental.profesor.infrastructure.ProfesorRepository;
import org.control_parental.publicacion.domain.Publicacion;
import org.control_parental.publicacion.dto.PublicacionResponseDto;
import org.control_parental.salon.dto.NewSalonDTO;
import org.control_parental.salon.dto.SalonResponseDto;
import org.control_parental.salon.infrastructure.SalonRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SalonService {

    @Autowired
    private SalonRepository salonRepository;

    @Autowired
    private HijoRepository hijoRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ProfesorRepository profesorRepository;

    public void createSalon(NewSalonDTO newSalonDTO) {
        Optional<Salon> salon = salonRepository.findByNombre(newSalonDTO.getNombre());
        if (salon.isPresent()) throw new ResourceAlreadyExistsExpeption("El salon ya existe");
        Salon salon1 = modelMapper.map(newSalonDTO, Salon.class);
        salonRepository.save(salon1);
    }

    public SalonResponseDto getSalonById(Long id) {
        return modelMapper.map(salonRepository.findById(id).orElseThrow(), SalonResponseDto.class);
    }

    public void addStudentOrProfesorToSalon(Long id, Long spId) {
        Optional<Hijo> hijo = hijoRepository.findById(spId);
        Optional<Profesor> profesor = profesorRepository.findById(spId);
        Salon salon = salonRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("El salon no fue encontrado"));
        if (profesor.isEmpty() && hijo.isEmpty()) throw new ResourceNotFoundException("Esta entidad no existe");
        if (profesor.isPresent()) {
            salon.addProfesor(profesor.get());
            profesor.get().addSalon(salon);
            salonRepository.save(salon);
            profesorRepository.save(profesor);
        }
        if (hijo.isPresent()) {
            salon.addStudent(hijo.get());
            hijo.get().setSalon(salon);
            salonRepository.save(salon);
            hijoRepository.save(hijo);

        }

    }



    public List<ReducedHijoDto> getAllStudents(Long id) {
        Salon salon = salonRepository.findById(id).orElseThrow();
        List<Hijo> hijos = salon.getHijos();
        List<ReducedHijoDto> hijosDto = new ArrayList<ReducedHijoDto>();
        hijos.forEach(hijo -> {
            hijosDto.add(modelMapper.map(hijo, ReducedHijoDto.class));
        });
        return hijosDto;
    }

    public List<PublicacionResponseDto> getAllPublicaciones(Long id) {
        Salon salon = salonRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("El salon no fue encontrado"));
        List<Publicacion> publicaciones = salon.getPublicaciones();
        List<PublicacionResponseDto> publicacionesDto = new ArrayList<>();
        publicaciones.forEach(publicacion -> {publicacionesDto.add(modelMapper.map(publicacion, PublicacionResponseDto.class));});
        return publicacionesDto;
    }


}
