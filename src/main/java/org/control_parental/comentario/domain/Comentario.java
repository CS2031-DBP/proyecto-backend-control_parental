package org.control_parental.comentario.domain;

import jakarta.persistence.*;
import lombok.Data;
import org.control_parental.padre.domain.Padre;
import org.control_parental.publicacion.domain.Publicacion;

import java.time.LocalDateTime;

@Data
@Entity
public class Comentario {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    LocalDateTime fecha;

    String contenido;

    @ManyToOne
    Padre padre;

    @ManyToOne
    Publicacion publicacion;
}
