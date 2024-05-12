package org.control_parental.padre.domain;

import org.control_parental.hijo.domain.Hijo;
import org.control_parental.padre.dto.NewPadreDto;
import org.control_parental.padre.dto.PadreResponseDto;
import org.control_parental.padre.dto.PadreSelfResponseDto;
import org.control_parental.padre.infrastructure.PadreRepository;
import org.control_parental.usuario.NewPasswordDto;
import org.control_parental.usuario.domain.Role;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public PadreSelfResponseDto getOwnInfo() {
        String email = "email@email.com";
        Padre padre = padreRepository.findByEmail(email).orElseThrow();
        return modelMapper.map(padre, PadreSelfResponseDto.class);
    }

    public void deletePadre(Long id) {
        padreRepository.deleteById(id);
    }

    public List<Hijo> getHijos(Long id) {
        Padre padre = padreRepository.findById(id).orElseThrow();
        return padre.getHijos();
    }

    public List<Hijo> getOwnHijos() {
        String email = "email@email.com";
        Padre padre = padreRepository.findByEmail(email).orElseThrow();
        return padre.getHijos();
    }

    public void newPassword(NewPasswordDto newPasswordDto){
        Padre padre = padreRepository.findByEmail(newPasswordDto.getEmail()).orElseThrow();
        padre.setPassword(newPasswordDto.getPassword());
        padreRepository.save(padre);
    }

}
