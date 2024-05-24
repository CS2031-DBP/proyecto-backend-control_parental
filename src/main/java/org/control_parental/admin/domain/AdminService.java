package org.control_parental.admin.domain;

import org.control_parental.admin.dto.NewAdminDto;
import org.control_parental.admin.infrastructure.AdminRepository;
import org.control_parental.exceptions.ResourceAlreadyExistsException;
import org.control_parental.usuario.domain.Role;
import org.control_parental.usuario.domain.Usuario;
import org.control_parental.usuario.domain.UsuarioService;
import org.control_parental.usuario.infrastructure.UsuarioRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AdminService {
    @Autowired
    ModelMapper modelMapper;

    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UsuarioRepository<Usuario> usuarioRepository;

    public void createAdmin(NewAdminDto newAdminDto) {
        Admin admin = modelMapper.map(newAdminDto, Admin.class);
        if(usuarioRepository.findByEmail(newAdminDto.getEmail()).isPresent()) {
            throw new ResourceAlreadyExistsException("El usuario ya existe");
        }
        admin.setRole(Role.ADMIN);
        admin.setPassword(passwordEncoder.encode(newAdminDto.getPassword()));
        adminRepository.save(admin);
    }
}
