package org.control_parental.publicacion.dto;

import org.control_parental.comentario.domain.Comentario;
import org.control_parental.hijo.domain.Hijo;
import org.control_parental.padre.domain.Padre;
import org.control_parental.profesor.domain.Profesor;
import org.control_parental.salon.domain.Salon;

import java.time.LocalDateTime;
import java.util.List;

public class PostRequestDTO {


    LocalDateTime fecha;

    String descripcion;

    Integer likes;

    String foto;

    String titulo;

    Profesor profesor;

    List<Hijo> hijos;

    List<Comentario> comentarios;

    List<Padre> likers;

    Salon salon;
}
