package org.control_parental.padre.domain;

import jakarta.persistence.*;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.control_parental.comentario.domain.Comentario;
import org.control_parental.hijo.domain.Hijo;
import org.control_parental.publicacion.domain.Publicacion;
import org.control_parental.usuario.domain.Usuario;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Padre extends Usuario {

    @Column(nullable = false)
    @Size(min = 9, max = 15)
    String phoneNumber;

    @ManyToMany(cascade = CascadeType.ALL) // si eliminamos al padre tambien se eliminaran a los hijos relacionados al padre
    List<Hijo> hijos;

    @OneToMany
    List<Comentario> comentarios;

    @ManyToMany
    List<Publicacion> posts_likeados;
}
