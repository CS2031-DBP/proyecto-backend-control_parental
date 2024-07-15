package org.control_parental.salon.domain;

import org.control_parental.admin.domain.Admin;
import org.control_parental.admin.infrastructure.AdminRepository;
import org.control_parental.auth.AuthorizationUtils;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    private AdminRepository adminRepository;

    @Autowired
    private ModelMapper modelMapper;
  
    @Autowired
    private ProfesorRepository profesorRepository;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    private AuthorizationUtils authorizationUtils;

    public String createSalon(NewSalonDTO newSalonDTO) {
        String email = authorizationUtils.authenticateUser();

        Admin admin = adminRepository.findByEmail(email).orElseThrow(()-> new ResourceNotFoundException("No se encontro al admin"));

        Optional<Salon> salon = salonRepository.findByNombre(newSalonDTO.getNombre());
        if (salon.isPresent()) throw new ResourceAlreadyExistsException("El salon ya existe");
        Salon salon1 = modelMapper.map(newSalonDTO, Salon.class);
        salon1.setNido(admin.getNido());
        salonRepository.save(salon1);
      
        return "/" + salon1.getId();
    }

    public SalonResponseDto getSalonById(Long id) {
        return modelMapper.map(salonRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("El salon no fue encontrado")), SalonResponseDto.class);
    }

    public List<SalonResponseDto> getAllSalones(int page, int size) {
        String email = authorizationUtils.authenticateUser();
        Pageable pageable = PageRequest.of(page, size);

        Page<Salon> salones = salonRepository.findAllBy(pageable);
        List<SalonResponseDto> responseDtos = new ArrayList<>();
        salones.forEach(salon -> {responseDtos.add(modelMapper.map(salon, SalonResponseDto.class));});

        return responseDtos;

    }

    public void addHijo(Long idSalon, Long idHijo) {
        String email = authorizationUtils.authenticateUser();
        Hijo hijo = hijoRepository.findById(idHijo).orElseThrow(() -> new ResourceNotFoundException("Hijo no encontrado"));
        Salon salon = salonRepository.findById(idSalon).orElseThrow(() -> new ResourceNotFoundException("El salon no fue encontrado"));
        salon.addStudent(hijo);
        hijo.setSalon(salon);
        applicationEventPublisher.publishEvent(
                new AgregacionHijoSalonEmailEvent(this, hijo.getNombre(),
                        hijo.getPadre().getEmail(),
                        salon.getNombre(),
                        hijo.getPadre().getNombre())
        );
        salonRepository.save(salon);
        hijoRepository.save(hijo);
    }

    public void addProfesor(Long idSalon, Long idProfesor) {
        String email = authorizationUtils.authenticateUser();
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

    public void deleteProfesorFromSalon(Long salonId, Long profesorId) {
        String Email = authorizationUtils.authenticateUser();
        Profesor profesor = profesorRepository.findById(profesorId).orElseThrow(()-> new ResourceNotFoundException("No se encontro al profesor"));
        Salon salon = salonRepository.findById(salonId).orElseThrow(()-> new ResourceNotFoundException("No se encontro al salon"));
        salon.removeProfesor(profesor);
        profesor.removeSalon(salon);
        salonRepository.save(salon);
        profesorRepository.save(profesor);
    }

    public void deleteStudentFromSalon(Long salonId, Long studentId) {
        String email = authorizationUtils.authenticateUser();
        Hijo hijo = hijoRepository.findById(studentId).orElseThrow(()-> new ResourceNotFoundException("No se encontro al hijo"));
        Salon salon = salonRepository.findById(salonId).orElseThrow(()-> new ResourceNotFoundException("No se encontro al salon"));
        salon.removeStudent(hijo);
        hijo.setSalon(null);
        salonRepository.save(salon);
        hijoRepository.save(hijo);
    }


}
