package org.control_parental.post.domain;

import jakarta.persistence.*;
import lombok.Data;
import org.control_parental.comentario.domain.Comentario;
import org.control_parental.hijo.domain.Hijo;
import org.control_parental.padre.domain.Padre;
import org.control_parental.profesor.Profesor;
import org.control_parental.salon.domain.Salon;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    LocalDateTime fecha;

    String descripcion;

    Integer likes;

    String foto;

    String titulo;

    @ManyToOne
    Profesor profesor;

    @ManyToMany
    List<Hijo> hijos;

    @OneToMany
    List<Comentario> comentarios;

    @ManyToMany
    List<Padre> likers;

    @ManyToOne
    Salon salon;

}
