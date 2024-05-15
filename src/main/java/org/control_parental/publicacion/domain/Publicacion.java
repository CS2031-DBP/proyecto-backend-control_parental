package org.control_parental.publicacion.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Value;
import org.control_parental.comentario.domain.Comentario;
import org.control_parental.hijo.domain.Hijo;
import org.control_parental.padre.domain.Padre;
import org.control_parental.profesor.domain.Profesor;
import org.control_parental.salon.domain.Salon;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
public class Publicacion {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    @Column
    LocalDateTime fecha;

    @Column(nullable = false)
    @Size(min = 0, max = 500)
    String descripcion;

    @Column(nullable = false)
    @Min(0)
    Integer likes;

    @Column
    String foto;

    @Column(nullable = false)
    @Size(min = 1, max = 255)
    String titulo;
/*
    @ManyToOne
    Profesor profesor;

    @ManyToMany
    List<Hijo> hijos;
*/
    @OneToMany
    List<Comentario> comentarios;
/*
    @ManyToMany
    List<Padre> likers;
*/
    @ManyToOne
    Salon salon;

}
