package org.control_parental.comentario.domain;

import org.control_parental.comentario.dto.ComentarioResponseDto;
import org.control_parental.comentario.dto.NewComentarioDto;
import org.control_parental.comentario.infrastructure.ComentarioRepository;
import org.control_parental.exceptions.ResourceNotFoundException;
import org.control_parental.publicacion.domain.Publicacion;
import org.control_parental.publicacion.dto.PublicacionResponseDto;
import org.control_parental.publicacion.infrastructure.PublicacionRepository;
import org.control_parental.usuario.domain.Usuario;
import org.control_parental.usuario.dto.UsuarioResponseDto;
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
    @Autowired
    private PublicacionRepository publicacionRepository;

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

    public void postComentario(NewComentarioDto newComentarioDto, Long IdPublicacion) {
        //conseguir el id del usuario actual
        Long usuarioId = 1L;
        Comentario newComentario = modelMapper.map(newComentarioDto, Comentario.class);
        Publicacion publicacion = publicacionRepository.findById(IdPublicacion).orElseThrow(()-> new ResourceNotFoundException("No se encontro estas publicacion"));
        Usuario usuario = usuarioRepository.findById(usuarioId).orElseThrow();
        usuario.getComentarios().add(newComentario);
        publicacion.getComentarios().add(newComentario);

        newComentario.setFecha(LocalDateTime.now());
        newComentario.setUsuario(usuario);
        newComentario.setPublicacion(publicacion);

        comentarioRepository.save(newComentario);
    }

    public ComentarioResponseDto getComentarioById(Long id) {
        Comentario comentario = comentarioRepository.findById(id).orElseThrow();
        UsuarioResponseDto usuarioResponseDto = modelMapper.map(comentario.getUsuario(), UsuarioResponseDto.class);
        PublicacionResponseDto publicacionResponseDto = modelMapper.map(comentario.getPublicacion(), PublicacionResponseDto.class);
        ComentarioResponseDto comentarioResponseDto = new ComentarioResponseDto();

        comentarioResponseDto.setFecha(comentario.getFecha());
        comentarioResponseDto.setContenido(comentario.getContenido());
        comentarioResponseDto.setUsuario(usuarioResponseDto);
        comentarioResponseDto.setPublicacion(publicacionResponseDto);
        return comentarioResponseDto;
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
