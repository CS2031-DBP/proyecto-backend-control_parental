package org.control_parental.padre.domain;

import org.control_parental.configuration.AuthorizationUtils;
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

import java.nio.file.AccessDeniedException;
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
    @Autowired
    AuthorizationUtils authorizationUtils;

    public String savePadre(NewPadreDto newPadreDto) {
        String enail = authorizationUtils.authenticateUser();

        Padre padre = modelMapper.map(newPadreDto, Padre.class);
        if(usuarioRepository.findByEmail(newPadreDto.getEmail()).isPresent()) {
            throw new ResourceAlreadyExistsException("el usuario ya existe");
        }
        padre.setPassword(passwordEncoder.encode(newPadreDto.getPassword()));
        padre.setRole(Role.PADRE);
        applicationEventPublisher.publishEvent(
                new NuevoUsuarioEmailEvent(this, padre.getEmail(),
                        newPadreDto.getPassword(),
                        padre.getNombre(),
                        padre.getRole().toString())
        );
        padreRepository.save(padre);
        return "/"+padre.getId();
    }

    public PadreResponseDto getPadreById(Long id) {
        Padre padre  = padreRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("El padre no fue encontrado"));

        return modelMapper.map(padre, PadreResponseDto.class);
    }

    public PadreSelfResponseDto getOwnInfo() {
        String email = authorizationUtils.authenticateUser();
        Padre padre = padreRepository.findByEmail(email).orElseThrow(
                ()-> new ResourceNotFoundException("Padre no fue encontrado")
        );
        return modelMapper.map(padre, PadreSelfResponseDto.class);
    }

    public void deletePadre(Long id) throws AccessDeniedException {
        String userEmail = authorizationUtils.authenticateUser();
        authorizationUtils.verifyUserAuthorization(userEmail, id);
        padreRepository.deleteById(id);
    }

    public List<Hijo> getHijos(Long id) {
        Padre padre = padreRepository.findById(id).orElseThrow();
        return padre.getHijos();
    }

    public List<Hijo> getOwnHijos() {
        String email = authorizationUtils.authenticateUser();
        Padre padre = padreRepository.findByEmail(email).orElseThrow(
                ()-> new ResourceNotFoundException("Padre no fue encontrado")
        );
        return padre.getHijos();
    }

    public void newPassword(NewPasswordDto newPasswordDto){
        Padre padre = padreRepository.findByEmail(newPasswordDto.getEmail()).orElseThrow(() -> new ResourceNotFoundException("No existe el usuario"));
        padre.setPassword(newPasswordDto.getPassword());
        Date date = new Date();
        applicationEventPublisher.publishEvent(
                new NuevaContaseñaEmailEvent(padre.getNombre(), padre.getEmail(), date)
        );
        padreRepository.save(padre);
    }

}



