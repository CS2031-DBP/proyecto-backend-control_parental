package org.control_parental.padre.domain;

import jakarta.persistence.*;

import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.control_parental.hijo.domain.Hijo;
import org.control_parental.like.Domain.Like;
import org.control_parental.publicacion.domain.Publicacion;
import org.control_parental.usuario.domain.Usuario;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class Padre extends Usuario {

    @Column(nullable = false)
    @Size(min = 9, max = 15)
    String phoneNumber;

    @OneToMany(mappedBy = "padre", cascade = CascadeType.REMOVE) // si eliminamos al padre tambien se eliminaran a los hijos relacionados al padre
    List<Hijo> hijos = new ArrayList<>();

    @OneToMany(mappedBy = "like", cascade = CascadeType.REMOVE)
    List<Like> posts_likeados = new ArrayList<>();

    public void addHijo(Hijo hijo) {
        hijos.add(hijo);
    }

    public void addLike(Like like) {posts_likeados.add(like);}
}
