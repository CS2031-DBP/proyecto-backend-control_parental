package org.control_parental.salon.domain;

import org.control_parental.hijo.domain.Hijo;
import org.control_parental.hijo.Dto.NewHijoDto;
import org.control_parental.hijo.Dto.ResponseHijoDto;
import org.control_parental.hijo.infrastructure.HijoRepository;
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

    public Salon createSalon(NewSalonDTO newSalonDTO) {
        Salon salon = modelMapper.map(newSalonDTO, Salon.class);
        salonRepository.save(salon);
        return salon;
    }

    public Salon getSalonById(Long id) {
        return salonRepository.findById(id).orElseThrow();
    }

    public void addStudentToSalon(Long id, NewHijoDto hijoDTO) {
        Hijo hijo = hijoRepository.findByNombreAndApellido(hijoDTO.getNombre(), hijoDTO.getApellido()).orElseThrow();
        Salon salon = salonRepository.findById(id).orElseThrow();
        hijo.setSalon(salon);
        salon.addStudent(hijo);
        salonRepository.save(salon);
    }

    public List<ResponseHijoDto> getAllStudents(Long id) {
        Salon salon = salonRepository.findById(id).orElseThrow();
        List<Hijo> hijos = salon.getAllStudents();
        List<ResponseHijoDto> hijosDto = new ArrayList<ResponseHijoDto>();
        hijos.forEach(hijo -> {
            hijosDto.add(modelMapper.map(hijo, ResponseHijoDto.class));
        });


        return hijosDto;

    }
}
