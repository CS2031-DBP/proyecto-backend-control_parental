package org.control_parental.hijo.domain;

import org.control_parental.csv.CSVHelper;
import org.control_parental.exceptions.ResourceNotFoundException;
import org.control_parental.hijo.dto.HijoResponseDto;
import org.control_parental.hijo.dto.NewHijoDto;
import org.control_parental.hijo.infrastructure.HijoRepository;
import org.control_parental.padre.domain.Padre;
import org.control_parental.padre.dto.PadreResponseDto;
import org.control_parental.padre.infrastructure.PadreRepository;
import org.control_parental.publicacion.domain.Publicacion;
import org.control_parental.publicacion.dto.PublicacionResponseDto;
import org.control_parental.publicacion.infrastructure.PublicacionRepository;
import org.control_parental.salon.domain.Salon;
import org.control_parental.salon.infrastructure.SalonRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
public class HijoService {
    @Autowired
    private HijoRepository hijoRepository;

    @Autowired
    private SalonRepository salonRepository;

    @Autowired
    private PublicacionRepository publicacionRepository;

    @Autowired
    private PadreRepository padreRepository;

    @Autowired
    ModelMapper modelMapper;

    public void saveCSVStudents(MultipartFile file) throws IOException {
        var inputStream =  file.getInputStream();
        List<NewHijoDto> hijos = CSVHelper.csvToHijos( inputStream);
        List<Hijo> newHijos = new ArrayList<Hijo>();
        hijos.forEach(hijo -> newHijos.add(modelMapper.map(hijo, Hijo.class)));
        hijoRepository.saveAll(newHijos);
    }

    public List<HijoResponseDto> getHijos() {
        List<HijoResponseDto> newHijos = new ArrayList<HijoResponseDto>();
        List<Hijo> hijos = hijoRepository.findAll();
        hijos.forEach(hijo -> newHijos.add(modelMapper.map(hijo, HijoResponseDto.class)));
        return newHijos;
    }

    public void createStudent(NewHijoDto newHijoDto, Long idPadre){
        Padre padre = padreRepository.findById(idPadre).orElseThrow(() -> new ResourceNotFoundException("El padre no fue encontrado"));
        Hijo hijo = modelMapper.map(newHijoDto, Hijo.class);
        hijo.setPadre(padre);
        hijoRepository.save(hijo);
    }

    public HijoResponseDto getStudentById(Long id){
        Hijo hijo = hijoRepository.findById(id).orElseThrow();
        return modelMapper.map(hijo, HijoResponseDto.class);
    }

    public void deleteHijo(Long id) {
        hijoRepository.deleteById(id);
    }

    /*
    //Busqueda de publicaciones de un hijo
    public List<PublicacionResponseDto> getPublicaciones(Long id){
        Hijo hijo = hijoRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("El hijo no fue encontrado"));
        Salon salon = salonRepository.findById(hijo.getSalon().getId()).orElseThrow(() -> new ResourceNotFoundException("El salon no fue encontrado"));
        List<Publicacion> publicaciones = publicacionRepository.findAllBySalon(salon);
        List<PublicacionResponseDto> publicacionesHijo = new ArrayList<>();
        publicaciones.forEach(publicacion -> {
            publicacion.getHijos().forEach(newHijo -> {
                if(newHijo.getId().equals(hijo.getId())){ publicacionesHijo.add(modelMapper.map(publicacion, PublicacionResponseDto.class));}
            });
        });
        return publicacionesHijo;
    }
     */
}
