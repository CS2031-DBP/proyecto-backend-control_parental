package org.control_parental.hijo.domain;

import org.control_parental.csv.CSVHelper;
import org.control_parental.hijo.dto.HijoResponseDto;
import org.control_parental.hijo.dto.NewHijoDto;
import org.control_parental.hijo.infrastructure.HijoRepository;
import org.control_parental.padre.domain.Padre;
import org.control_parental.publicacion.domain.Publicacion;
import org.control_parental.publicacion.infrastructure.PublicacionRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class HijoService {
    @Autowired
    private HijoRepository hijoRepository;

    @Autowired
    private PublicacionRepository publicacionRepository;

    @Autowired
    ModelMapper modelMapper;

    public void saveCSVStudents(MultipartFile file) throws IOException {
        List<NewHijoDto> hijos = CSVHelper.csvToHijos(file.getInputStream());
        List<Hijo> newHijos = new ArrayList<Hijo>();
        hijos.forEach(hijo -> newHijos.add(modelMapper.map(hijo, Hijo.class)));
        hijoRepository.saveAll(newHijos);
    }

    public List<Hijo> getHijos() {
        return hijoRepository.findAll();
    }

    public void updateStudent(Long id, NewHijoDto newHijoDto) {
        Hijo hijo = hijoRepository.findById(id).orElseThrow();
        hijo.setNombre(newHijoDto.getNombre());
        hijo.setApellido(newHijoDto.getApellido());
        hijo.setPadre(modelMapper.map(newHijoDto.getPadre(), Padre.class));
        hijoRepository.save(hijo);
    }

    public void createStudent(NewHijoDto newHijoDto){
        hijoRepository.save(modelMapper.map(newHijoDto, Hijo.class));
    }

    public HijoResponseDto getStudentById(Long id){
        Hijo hijo = hijoRepository.findById(id).orElseThrow();
        return modelMapper.map(hijo, HijoResponseDto.class);
    }

    public void deleteHijo(Long id) {
        hijoRepository.deleteById(id);
    }
/*
    public List<Publicacion> getPublicaciones(Long id){

    }*/
}
