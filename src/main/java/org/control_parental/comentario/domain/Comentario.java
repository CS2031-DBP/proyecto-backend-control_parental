package org.control_parental.comentario.domain;

import io.micrometer.common.lang.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.control_parental.padre.domain.Padre;
import org.control_parental.publicacion.domain.Publicacion;
import org.control_parental.reply.domain.Reply;
import org.control_parental.usuario.domain.Usuario;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class Comentario {
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
    Publicacion publicacion;

    @OneToMany
    List<Reply> replies = new ArrayList<>();
}
