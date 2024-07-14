package org.control_parental.usuario.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.control_parental.comentario.domain.Comentario;
import org.control_parental.nido.Domain.Nido;
import org.control_parental.reply.domain.Reply;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Data
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class Usuario implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    @Column(nullable = false, unique = true)
    @Email
    String email;

    @Column(nullable = false)
//    @Size(min = 6, max = 50)
    String password;

    @Column(nullable = false)
    @Size(min = 2, max = 100)
    String nombre;

    @Column(nullable = false)
    @Size(min = 2, max = 100)
    String apellido;

    @Column
    Role role;

    @OneToMany
    List<Comentario> comentarios = new ArrayList<>();

    @OneToMany
    List<Reply> replies = new ArrayList<>();

    @JoinColumn(nullable = false)
    @ManyToOne
    Nido nido;

    @Transient
    private String rolePrefix = "ROLE_";

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(rolePrefix + role.name()));
    }

    @Override
    public String getUsername(){
        return email;
    }

    @Override
    public boolean isAccountNonExpired(){return true;};

    @Override
    public boolean isAccountNonLocked(){return true;};

    @Override
    public boolean isCredentialsNonExpired(){return true;};

    @Override
    public boolean isEnabled(){return true;};
}
