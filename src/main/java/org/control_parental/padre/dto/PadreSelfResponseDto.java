package org.control_parental.padre.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.control_parental.comentario.dto.ComentarioResponseDto;
import org.control_parental.hijo.dto.HijoResponseDto;
import org.control_parental.hijo.dto.PadreHijoResponseDto;
import org.control_parental.publicacion.dto.PublicacionResponseDto;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PadreSelfResponseDto {

    Long id;

    @NotNull
    String phoneNumber;

    @NotNull
    String email;

    @NotNull
    String nombre;

    @NotNull
    String apellido;

    @JsonIgnoreProperties("padre")
    List<PadreHijoResponseDto> hijos;

    //@JsonIgnoreProperties("padre")
    //List<ComentarioResponseDto> comentarios;

    //@JsonIgnoreProperties("likers")
    //List<PublicacionResponseDto> publicaciones_likeadas;
}
