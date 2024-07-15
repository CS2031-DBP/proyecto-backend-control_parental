package org.control_parental.reply.domain;

import org.control_parental.comentario.dto.ComentarioResponseDto;
import org.control_parental.comentario.infrastructure.ComentarioRepository;
import org.control_parental.publicacion.domain.Publicacion;
import org.control_parental.reply.domain.Reply;
import org.control_parental.reply.dto.ReplyResponseDto;
import org.control_parental.reply.dto.NewReplyDto;
import org.control_parental.reply.dto.ReplySmallResponseDto;
import org.control_parental.reply.infrastructure.ReplyRepository;
import org.control_parental.configuration.AuthorizationUtils;
import org.control_parental.exceptions.ResourceNotFoundException;
import org.control_parental.comentario.domain.Comentario;
import org.control_parental.comentario.infrastructure.ComentarioRepository;
import org.control_parental.usuario.domain.Usuario;
import org.control_parental.usuario.infrastructure.UsuarioRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class ReplyService {
    @Autowired
    private ReplyRepository replyRepository;

    @Autowired
    private UsuarioRepository<Usuario> usuarioRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ComentarioRepository comentarioRepository;

    @Autowired
    private AuthorizationUtils authorizationUtils;

//    public void checkUser(Long Reply_id){
//        //SecurityContext
//        String email = "email@gmail.com";
//        Usuario user = usuarioRepository.findByEmail(email).orElseThrow();
//        //if(user.getRole().equals(Role.))
//        //checkear si el reply le corresponde al propio usuario
//        Reply reply = replyRepository.findById(Reply_id).orElseThrow();
//        if(!reply.getId().equals(user.getId())) {
//            throw new RuntimeException();
//        }
//    }

    public String postReply(NewReplyDto newReplyDto, Long IdComentario) {
        String email = authorizationUtils.authenticateUser();

        Reply newReply = modelMapper.map(newReplyDto, Reply.class);
        Comentario comentario = comentarioRepository.findById(IdComentario).orElseThrow(()-> new ResourceNotFoundException("No se encontro este comentario"));
        Usuario usuario = usuarioRepository.findByEmail(email).orElseThrow();
        usuario.getReplies().add(newReply);
        comentario.getReplies().add(newReply);

        newReply.setFecha(LocalDateTime.now());
        newReply.setUsuario(usuario);
        newReply.setComentario(comentario);

        replyRepository.save(newReply);
        return "/" + newReply.getId();
    }

    public ReplyResponseDto getReplyById(Long id) {
        Reply reply = replyRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Reply no encontrado"));

        return modelMapper.map(reply, ReplyResponseDto.class);
    }

    public void patchReply(Long id, NewReplyDto newReplyDto) throws AccessDeniedException {
        String email = authorizationUtils.authenticateUser();
        Reply reply = replyRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Reply no encontrado"));
        Long userId = reply.getUsuario().getId();
        authorizationUtils.verifyUserAuthorization(email, userId);
        reply.setContenido(newReplyDto.getContenido());
        reply.setFecha(LocalDateTime.now());
        replyRepository.save(reply);
    }

    public void deleteReplyById(Long id) throws AccessDeniedException {
        String userEmail = authorizationUtils.authenticateUser();
        Usuario usuarioEmail = usuarioRepository.findByEmail(userEmail).orElseThrow(
                ()-> new ResourceNotFoundException("Usuario no encontrado"));
        Reply reply = replyRepository.findById(id).orElseThrow(
                ()-> new ResourceNotFoundException("Reply no encontrado"));
        if (!Objects.equals(usuarioEmail.getEmail(), reply.getUsuario().getEmail())
                && !Objects.equals(usuarioEmail.getRole().toString(), "ADMIN")
                && !Objects.equals(usuarioEmail.getRole().toString(), "PROFESOR"))
            throw new AccessDeniedException("No estas autorizado");
        replyRepository.deleteById(id);
    }

    public List<ReplySmallResponseDto> getByComentarioId(Long id, int page, int size) {
        Comentario comentario = comentarioRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Comentario no encontrada"));

        Pageable pageable = PageRequest.of(page, size);

        Page<Reply> replyPage = replyRepository.findAllByComentario_IdOrderByFechaDesc(id, pageable);

        List<ReplySmallResponseDto> repliesData = new ArrayList<>();

        replyPage.forEach(reply -> {
            ReplySmallResponseDto res = modelMapper.map(reply, ReplySmallResponseDto.class);
            repliesData.add(res);
        });

        return repliesData;
    }


}
