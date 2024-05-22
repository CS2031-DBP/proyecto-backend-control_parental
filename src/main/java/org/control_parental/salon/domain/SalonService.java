package org.control_parental.salon.domain;

import org.control_parental.email.nuevoSalon.AgregacionHijoSalonEmailEvent;
import org.control_parental.exceptions.ResourceAlreadyExistsException;
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
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

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

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    public String createSalon(NewSalonDTO newSalonDTO) {
        Optional<Salon> salon = salonRepository.findByNombre(newSalonDTO.getNombre());
        if (salon.isPresent()) throw new ResourceAlreadyExistsException("El salon ya existe");
        Salon salon1 = modelMapper.map(newSalonDTO, Salon.class);
        salonRepository.save(salon1);
      
        return "/" + salon1.getId();
    }

    public SalonResponseDto getSalonById(Long id) {
        return modelMapper.map(salonRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("El salon no fue encontrado")), SalonResponseDto.class);
    }

    public void addHijo(Long idSalon, Long idHijo) {
        Hijo hijo = hijoRepository.findById(idHijo).orElseThrow(() -> new ResourceNotFoundException("Hijo no encontrado"));
        Salon salon = salonRepository.findById(idSalon).orElseThrow(() -> new ResourceNotFoundException("El salon no fue encontrado"));
        salon.addStudent(hijo);
        hijo.setSalon(salon);
        applicationEventPublisher.publishEvent(
                new AgregacionHijoSalonEmailEvent(this, hijo.getNombre(), hijo.getPadre().getEmail(), salon.getNombre())
        );
        salonRepository.save(salon);
        hijoRepository.save(hijo);
    }

    public void addProfesor(Long idSalon, Long idProfesor) {
        Profesor profesor = profesorRepository.findById(idProfesor).orElseThrow(() -> new ResourceNotFoundException("Profesor no encontrado"));
        Salon salon = salonRepository.findById(idSalon).orElseThrow(() -> new ResourceNotFoundException("El salon no fue encontrado"));
        salon.addProfesor(profesor);
        profesor.addSalon(salon);
        salonRepository.save(salon);
        profesorRepository.save(profesor);
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
