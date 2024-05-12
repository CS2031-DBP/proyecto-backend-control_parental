package org.control_parental.comentario.domain;

import org.control_parental.comentario.dto.ComentarioResponseDto;
import org.control_parental.comentario.dto.NewComentarioDto;
import org.control_parental.comentario.infrastructure.ComentarioRepository;
import org.control_parental.usuario.domain.Usuario;
import org.control_parental.usuario.infrastructure.UsuarioRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ComentarioService {
    @Autowired
    private ComentarioRepository comentarioRepository;

    @Autowired
    private UsuarioRepository<Usuario> usuarioRepository;

    @Autowired
    private ModelMapper modelMapper;

    public void checkUser(Long Comentario_id){
        //SecurityContext
        String email = "email@gmail.com";
        Usuario user = usuarioRepository.findByEmail(email).orElseThrow();
        //if(user.getRole().equals(Role.))
        //checkear si el comentario le corresponde al propio usuario
        Comentario comentario = comentarioRepository.findById(Comentario_id).orElseThrow();
        if(!comentario.getId().equals(user.getId())) {
            throw new RuntimeException();
        }
    }
    public void postComentario(NewComentarioDto newComentarioDto) {
        comentarioRepository.save(modelMapper.map(newComentarioDto, Comentario.class));
    }

    public ComentarioResponseDto getComentarioById(Long id) {
        Comentario comentario = comentarioRepository.findById(id).orElseThrow();
        return modelMapper.map(comentario, ComentarioResponseDto.class);
    }

    public void patchComentario(Long id, NewComentarioDto newComentarioDto){
        Comentario comentario = comentarioRepository.findById(id).orElseThrow();
        comentario.setContenido(newComentarioDto.getContenido());
        comentario.setFecha(LocalDateTime.now());
        comentarioRepository.save(comentario);
    }

    public void deleteComentarioById(Long id) {
        comentarioRepository.deleteById(id);
    }


}
