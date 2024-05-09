package org.control_parental.usuario.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Entity
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    @Column(nullable = false)
    @Size(min = 9, max = 15)
    String phoneNumber;

    @Column(nullable = false)
    @Email
    String email;

    @Column(nullable = false)
    @Size(min = 6, max = 50)
    String password;

    @Column(nullable = false)
    @Size(min = 2, max = 50)
    String nombre;

    @Column(nullable = false)
    @Size(min = 2, max = 50)
    String apellido;

    @Column(nullable = false)
    Role role;
}
