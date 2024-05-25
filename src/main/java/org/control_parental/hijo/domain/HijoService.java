package org.control_parental.hijo.domain;

import org.control_parental.configuration.AuthorizationUtils;
import org.control_parental.csv.CSVHelper;
import org.control_parental.exceptions.ResourceNotFoundException;
import org.control_parental.hijo.dto.HijoResponseDto;
import org.control_parental.hijo.dto.NewHijoDto;
import org.control_parental.hijo.infrastructure.HijoRepository;
import org.control_parental.padre.domain.Padre;
import org.control_parental.padre.infrastructure.PadreRepository;
import org.control_parental.publicacion.domain.Publicacion;
import org.control_parental.publicacion.dto.PublicacionResponseDto;
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
    private PadreRepository padreRepository;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    AuthorizationUtils authorizationUtils;

    public void saveCSVStudents(MultipartFile file) throws IOException {

        String email = authorizationUtils.authenticateUser();
        var inputStream =  file.getInputStream();
        List<NewHijoDto> hijos = CSVHelper.csvToHijos( inputStream);
        List<Hijo> newHijos = new ArrayList<Hijo>();
        hijos.forEach(hijo -> {
                    Hijo nuevoHijo = modelMapper.map(hijo, Hijo.class);
                    Padre padre = padreRepository.findByEmail(hijo.getEmail()).orElseThrow(
                            ()-> new ResourceNotFoundException("Este padre no fue encontrado"));
                    nuevoHijo.setPadre(padre);
                newHijos.add(nuevoHijo);
                });

        hijoRepository.saveAll(newHijos);
    }

    public List<HijoResponseDto> getHijos() {
        List<HijoResponseDto> newHijos = new ArrayList<>();
        List<Hijo> hijos = hijoRepository.findAll();
        hijos.forEach(hijo -> newHijos.add(modelMapper.map(hijo, HijoResponseDto.class)));
        return newHijos;
    }

    public String createStudent(NewHijoDto newHijoDto, Long idPadre){

        String email = authorizationUtils.authenticateUser();
    
        Padre padre = padreRepository.findById(idPadre).orElseThrow(() -> new ResourceNotFoundException("El padre no fue encontrado"));
        Hijo hijo = modelMapper.map(newHijoDto, Hijo.class);
        hijo.setPadre(padre);
        padre.addHijo(hijo);

        hijoRepository.save(hijo);
        padreRepository.save(padre);

        return "/" + hijo.getId();
    }

    public HijoResponseDto getStudentById(Long id){
        Hijo hijo = hijoRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("El hijo no fue encontrado"));
        return modelMapper.map(hijo, HijoResponseDto.class);
    }

    public void deleteHijo(Long id) {
        String email = authorizationUtils.authenticateUser();

        hijoRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("El hijo no fue encontrado"));
        hijoRepository.deleteById(id);
    }

    public List<PublicacionResponseDto> getPublicaciones(Long id) {
        List<Publicacion> publicaciones = hijoRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("El hijo no fue encontrado")).getPublicaciones();

        List<PublicacionResponseDto> publicacionesData = new ArrayList<>();

        for(Publicacion publicacion : publicaciones) {
            publicacionesData.add(modelMapper.map(publicacion, PublicacionResponseDto.class));
        }

        return publicacionesData;
    }

    public void updateStudent(Long id, NewHijoDto newHijo) {
        Hijo hijo = hijoRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("El hijo no fue encontrado"));

        if(newHijo.getNombre() != null) {
            hijo.setNombre(newHijo.getNombre());
        }
        if(newHijo.getApellido() != null) {
            hijo.setApellido(newHijo.getApellido());
        }
    }


}
