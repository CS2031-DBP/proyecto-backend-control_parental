package org.control_parental.publicacion.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.control_parental.comentario.domain.Comentario;
import org.control_parental.hijo.domain.Hijo;
import org.control_parental.like.Domain.Padre_Like;
import org.control_parental.profesor.domain.Profesor;
import org.control_parental.salon.domain.Salon;

import java.time.LocalDateTime;
import java.util.ArrayList;
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

    @ElementCollection
    @CollectionTable(name = "publicacion_fotos", joinColumns = @JoinColumn(name = "publicacion_id"))
    @Column(name = "foto")
    List<String> fotos = new ArrayList<>();

    @Column
    String ubicacion;

    @Column
    Double latitud;

    @Column
    Double longitud;

    @Column(nullable = false)
    @Size(min = 1, max = 255)
    String titulo;

    @JsonIgnoreProperties("publicaciones")
    @ManyToOne
    Profesor profesor;

    @ManyToMany(
            fetch = FetchType.LAZY,
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE
            }
    )
    List<Hijo> hijos = new ArrayList<>();

    @OneToMany
    List<Comentario> comentarios = new ArrayList<>();

    @OneToMany(mappedBy = "publicacion", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Padre_Like> likers = new ArrayList<>();

    @ManyToOne
    Salon salon;

    public void addLike(Padre_Like like) {
        likers.add(like);
        likes++;
    }

    public void quitarLike(Padre_Like like) {
        likers.remove(like);
        likes--;
    }

    public void addFoto(String foto) {fotos.add(foto);}
}
