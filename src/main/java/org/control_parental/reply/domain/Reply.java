package org.control_parental.reply.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.control_parental.comentario.domain.Comentario;
import org.control_parental.publicacion.domain.Publicacion;
import org.control_parental.usuario.domain.Usuario;

import java.time.LocalDateTime;

@Data
@Entity
public class Reply {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    @Column(name = "fecha", nullable = false)
    LocalDateTime fecha;

    @Column(name = "contenido", nullable = false)
    @Size(min = 1, max = 500)
    String contenido;

    @ManyToOne
    Usuario usuario;

    @ManyToOne
    Comentario comentario;
}
