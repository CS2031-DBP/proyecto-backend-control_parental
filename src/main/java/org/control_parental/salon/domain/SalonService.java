package org.control_parental.salon.domain;

import org.control_parental.hijo.domain.Hijo;
import org.control_parental.hijo.dto.NewHijoDto;
import org.control_parental.hijo.dto.HijoResponseDto;
import org.control_parental.hijo.infrastructure.HijoRepository;
import org.control_parental.publicacion.domain.Publicacion;
import org.control_parental.publicacion.dto.PublicacionResponseDto;
import org.control_parental.salon.dto.NewSalonDTO;
import org.control_parental.salon.dto.SalonResponseDto;
import org.control_parental.salon.infrastructure.SalonRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SalonService {

    @Autowired
    private SalonRepository salonRepository;

    @Autowired
    private HijoRepository hijoRepository;

    @Autowired
    private ModelMapper modelMapper;

    public void createSalon(NewSalonDTO newSalonDTO) {
        Salon salon = modelMapper.map(newSalonDTO, Salon.class);
        salonRepository.save(salon);
    }

    public SalonResponseDto getSalonById(Long id) {
        return modelMapper.map(salonRepository.findById(id).orElseThrow(), SalonResponseDto.class);
    }

    public void addStudentToSalon(Long id, NewHijoDto hijoDTO) {
        Hijo hijo = hijoRepository.findByNombreAndApellido(hijoDTO.getNombre(), hijoDTO.getApellido()).orElseThrow();
        Salon salon = salonRepository.findById(id).orElseThrow();
        hijo.setSalon(salon);
        salon.addStudent(hijo);
        salonRepository.save(salon);
    }

    public List<HijoResponseDto> getAllStudents(Long id) {
        Salon salon = salonRepository.findById(id).orElseThrow();
        List<Hijo> hijos = salon.getHijos();
        List<HijoResponseDto> hijosDto = new ArrayList<HijoResponseDto>();
        hijos.forEach(hijo -> {
            hijosDto.add(modelMapper.map(hijo, HijoResponseDto.class));
        });
        return hijosDto;
    }

    public List<PublicacionResponseDto> getAllPublicaciones(Long id) {
        Salon salon = salonRepository.findById(id).orElseThrow();
        List<Publicacion> publicaciones = salon.getPublicaciones();
        List<PublicacionResponseDto> publicacionesDto = new ArrayList<>();
        publicaciones.forEach(publicacion -> {publicacionesDto.add(modelMapper.map(publicacion, PublicacionResponseDto.class));});
        return publicacionesDto;
    }
}
