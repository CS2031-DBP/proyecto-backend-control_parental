package org.control_parental.publicacion.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.control_parental.comentario.dto.ComentarioResponseDto;
import org.control_parental.hijo.dto.HijoResponseDto;
import org.control_parental.padre.dto.PadreResponseDto;
import org.control_parental.profesor.dto.ProfesorResponseDto;
import org.control_parental.salon.dto.SalonResponseDto;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PublicacionResponseDto {

    LocalDateTime fecha;

    String descripcion;

    Integer likes;

    String foto;

    String titulo;

    ProfesorResponseDto profesor;

    @JsonIgnoreProperties("publicaciones")
    List<HijoResponseDto> hijos;

    @JsonIgnoreProperties("publicacion")
    List<ComentarioResponseDto> comentarios;

    List<PadreResponseDto> likers;

    @JsonIgnoreProperties("publicaciones")
    SalonResponseDto salon;
}
