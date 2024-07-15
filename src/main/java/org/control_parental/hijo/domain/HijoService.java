package org.control_parental.hijo.domain;

import org.control_parental.admin.infrastructure.AdminRepository;
import org.control_parental.auth.AuthorizationUtils;
import org.control_parental.csv.CSVHelper;
import org.control_parental.exceptions.ResourceNotFoundException;
import org.control_parental.hijo.dto.HijoResponseDto;
import org.control_parental.hijo.dto.NewHijoDto;
import org.control_parental.hijo.infrastructure.HijoRepository;
import org.control_parental.padre.domain.Padre;
import org.control_parental.padre.infrastructure.PadreRepository;
import org.control_parental.publicacion.domain.Publicacion;
import org.control_parental.publicacion.dto.PublicacionResponseDto;
import org.control_parental.publicacion.infrastructure.PublicacionRepository;
import org.control_parental.salon.domain.Salon;
import org.control_parental.salon.infrastructure.SalonRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    private AdminRepository adminRepository;

    @Autowired
    private SalonRepository salonRepository;

    @Autowired
    private PublicacionRepository publicacionRepository;

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

    public List<HijoResponseDto> getAllStudents(int page, int size) {
        String email = authorizationUtils.authenticateUser();
        Pageable pageable = PageRequest.of(page, size);
        Page<Hijo> hijos = hijoRepository.findAll(pageable);

        List<HijoResponseDto> responseDtos = new ArrayList<>();
        hijos.forEach(hijo -> responseDtos.add(modelMapper.map(hijo, HijoResponseDto.class)));

        return responseDtos;
    }

    public List<HijoResponseDto> getStudentsBySalon(Long salonId, int page, int size) {
        String email = authorizationUtils.authenticateUser();
        Salon salon = salonRepository.findById(salonId).orElseThrow(()-> new ResourceNotFoundException("El salon no fue encontrado"));

        Pageable pageable = PageRequest.of(page, size);
        Page<Hijo> hijos = hijoRepository.findAllBySalon(salon, pageable);

        List<HijoResponseDto> responseDtos = new ArrayList<>();
        hijos.forEach(hijo -> responseDtos.add(modelMapper.map(hijo, HijoResponseDto.class)));

        return responseDtos;
    }

    public String createStudent(NewHijoDto newHijoDto, Long idPadre){

        String email = authorizationUtils.authenticateUser();

        Padre padre = padreRepository.findById(idPadre).orElseThrow(() -> new ResourceNotFoundException("El padre no fue encontrado"));
        Hijo hijo = modelMapper.map(newHijoDto, Hijo.class);
        hijo.setPadre(padre);
        padre.addHijo(hijo);
        hijo.setNido(padre.getNido());


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

    public List<PublicacionResponseDto> getPublicaciones(Long id, int page, int size) {
        Hijo hijo = hijoRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("No se encontro al hijo"));

        Pageable pageable = PageRequest.of(page, size);

        Page<Publicacion> publicaciones = publicacionRepository.findAllByHijos(hijo, pageable);

        List<PublicacionResponseDto> response = new ArrayList<>();

        publicaciones.forEach(publicacion -> {
            PublicacionResponseDto res = modelMapper.map(publicacion, PublicacionResponseDto.class);
            response.add(res);
        });

        return response;
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
