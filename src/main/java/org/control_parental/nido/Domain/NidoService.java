package org.control_parental.nido.Domain;

import org.control_parental.auth.AuthorizationUtils;
import org.control_parental.exceptions.ResourceNotFoundException;
import org.control_parental.nido.Infrastructure.NidoRepository;
import org.control_parental.usuario.domain.Usuario;
import org.control_parental.usuario.infrastructure.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NidoService {

    @Autowired
    NidoRepository nidoRepository;

    @Autowired
    AuthorizationUtils authorizationUtils;

    @Autowired
    UsuarioRepository<Usuario> usuarioRepository;

    public String getName() {
        String email = authorizationUtils.authenticateUser();

        Usuario usuario = usuarioRepository.findByEmail(email).orElseThrow(()-> new ResourceNotFoundException("email no encontrado"));

        return usuario.getNido().getName();
    }

}
