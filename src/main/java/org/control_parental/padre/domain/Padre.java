package org.control_parental.padre.domain;

import jakarta.persistence.Entity;

import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.control_parental.comentario.domain.Comentario;
import org.control_parental.hijo.domain.Hijo;
import org.control_parental.post.domain.Post;
import org.control_parental.usuario.domain.Usuario;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Padre extends Usuario {

    @ManyToMany
    List<Hijo> hijos;

    @OneToMany
    List<Comentario> comentarios;

    @ManyToMany
    List<Post> posts_likeados;

}
