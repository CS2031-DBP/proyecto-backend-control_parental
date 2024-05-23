package org.control_parental.padre.domain;

import org.control_parental.email.nuevaContraseña.NuevaContaseñaEmailEvent;
import org.control_parental.email.nuevoUsuario.NuevoUsuarioEmailEvent;
import org.control_parental.exceptions.IllegalArgumentException;
import org.control_parental.exceptions.ResourceAlreadyExistsException;
import org.control_parental.exceptions.ResourceNotFoundException;
import org.control_parental.hijo.domain.Hijo;
import org.control_parental.padre.dto.NewPadreDto;
import org.control_parental.padre.dto.PadreResponseDto;
import org.control_parental.padre.dto.PadreSelfResponseDto;
import org.control_parental.padre.infrastructure.PadreRepository;
import org.control_parental.usuario.NewPasswordDto;
import org.control_parental.usuario.domain.Role;
import org.control_parental.usuario.domain.Usuario;
import org.control_parental.usuario.infrastructure.UsuarioRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class PadreService {
    @Autowired
    PadreRepository padreRepository;
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UsuarioRepository<Usuario> usuarioRepository;

    @Autowired
    ApplicationEventPublisher applicationEventPublisher;
    public void savePadre(NewPadreDto newPadreDto) {
        Padre padre = modelMapper.map(newPadreDto, Padre.class);
        if(usuarioRepository.findByEmail(newPadreDto.getEmail()).isPresent()) {
            throw new ResourceAlreadyExistsException("el usuario ya existe");
        }
        padre.setPassword(passwordEncoder.encode(newPadreDto.getPassword()));
        padre.setRole(Role.PADRE);
        applicationEventPublisher.publishEvent(
                new NuevoUsuarioEmailEvent(padre.getEmail(), padre.getPassword(), padre.getNombre(), padre.getRole().toString())
        );
        padreRepository.save(padre);

    }

    public PadreResponseDto getPadreById(Long id) {
        Padre padre  = padreRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("El padre no fue encontrado"));

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
        Date date = new Date();
        applicationEventPublisher.publishEvent(
                new NuevaContaseñaEmailEvent(padre.getNombre(), padre.getEmail(), date)
        );
        padreRepository.save(padre);
    }

}



