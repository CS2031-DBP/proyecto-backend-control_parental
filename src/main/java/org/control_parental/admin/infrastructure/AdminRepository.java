package org.control_parental.admin.infrastructure;

import org.control_parental.admin.domain.Admin;
import org.control_parental.usuario.infrastructure.UsuarioRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepository extends UsuarioRepository<Admin> {

}
