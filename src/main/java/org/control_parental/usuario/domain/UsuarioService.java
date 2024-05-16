package org.control_parental.usuario.domain;

import org.control_parental.usuario.dto.UsuarioResponseDto;
import org.control_parental.usuario.infrastructure.UsuarioRepository;
import org.hibernate.id.uuid.UuidGenerator;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UsuarioService {
    @Autowired
    private UsuarioRepository<Usuario> usuarioRepository;
    @Autowired
    private ModelMapper modelMapper;


    @Bean(name = "UserDetailsService")
    public UserDetailsService userDetailsService() {
        return username -> {
            Usuario user = usuarioRepository
                    .findByEmail(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));
            return (UserDetails) user;
        };
    }

    public List<UsuarioResponseDto> getAllUsuarios(){
        List<Usuario> usuarios = usuarioRepository.findAll();
        List<UsuarioResponseDto> usuariosDto = new ArrayList<>();
        usuarios.forEach(usuario -> {
            usuariosDto.add(modelMapper.map(usuario, UsuarioResponseDto.class));
        });
        return usuariosDto;
    }
}
