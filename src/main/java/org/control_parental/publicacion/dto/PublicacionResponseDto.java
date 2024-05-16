package org.control_parental.publicacion.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.control_parental.comentario.dto.ComentarioResponseDto;
import org.control_parental.hijo.dto.HijoPublicacionDto;
import org.control_parental.hijo.dto.HijoResponseDto;
import org.control_parental.padre.dto.PadreResponseDto;
import org.control_parental.profesor.dto.ProfesorPublicacionDto;
import org.control_parental.profesor.dto.ProfesorResponseDto;
import org.control_parental.salon.dto.SalonResponseDto;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PublicacionResponseDto {

    LocalDateTime fecha;

    @NotNull
    String descripcion;

    @NotNull
    Integer likes;

    String foto;

    @NotNull
    String titulo;

    ProfesorPublicacionDto profesor;

    List<HijoPublicacionDto> hijos;

//    @JsonIgnoreProperties("publicacion")
//    List<ComentarioResponseDto> comentarios;
/*
    List<PadreResponseDto> likers;
*/
//    @JsonIgnoreProperties("publicaciones")
    Long salonId;
}
