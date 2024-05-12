package org.control_parental.padre.domain;

import org.control_parental.padre.dto.NewPadreDto;
import org.control_parental.padre.dto.PadreResponseDto;
import org.control_parental.padre.infrastructure.PadreRepository;
import org.control_parental.usuario.domain.Role;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PadreService {
    @Autowired
    PadreRepository padreRepository;
    @Autowired
    private ModelMapper modelMapper;

    public void savePadre(NewPadreDto newPadreDto) {
        Padre padre = modelMapper.map(newPadreDto, Padre.class);
        padre.setRole(Role.PADRE);
        padreRepository.save(padre);

    }

    public PadreResponseDto getPadreById(Long id) {
        Padre padre  = padreRepository.findById(id).orElseThrow();

        return modelMapper.map(padre, PadreResponseDto.class);
    }



}
