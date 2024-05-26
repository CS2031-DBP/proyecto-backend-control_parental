package org.control_parental.like.Domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.control_parental.padre.domain.Padre;
import org.control_parental.publicacion.domain.Publicacion;

@Data
@NoArgsConstructor
@Entity
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class Padre_Like {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long Id;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "padre")
    Padre padre;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "likers")
    Publicacion publicacion;
}
