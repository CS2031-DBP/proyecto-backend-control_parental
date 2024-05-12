package org.control_parental.hijo.domain;

import org.control_parental.csv.CSVHelper;
import org.control_parental.hijo.dto.NewHijoDto;
import org.control_parental.hijo.infrastructure.HijoRepository;
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
    ModelMapper modelMapper;

    public void newStudent(NewHijoDto hijoDTO) {
        Hijo hijo = modelMapper.map(hijoDTO, Hijo.class);
        hijoRepository.save(hijo);
    }

    public void saveCSVStudents(MultipartFile file) throws IOException {
        List<NewHijoDto> hijos = CSVHelper.csvToHijos(file.getInputStream());
        List<Hijo> newHijos = new ArrayList<Hijo>();
        hijos.forEach(hijo -> newHijos.add(modelMapper.map(hijo, Hijo.class)));
        hijoRepository.saveAll(newHijos);
    }

    public List<Hijo> getHijos() {
        return hijoRepository.findAll();
    }

    public void deleteHijo(Long id) {
        hijoRepository.deleteById(id);
    }
}
