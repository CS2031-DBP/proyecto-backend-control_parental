package org.control_parental.admin.domain;

import org.control_parental.admin.dto.NewAdminDto;
import org.control_parental.admin.infrastructure.AdminRepository;
import org.control_parental.usuario.domain.Role;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminService {
    @Autowired
    ModelMapper modelMapper;

    @Autowired
    private AdminRepository adminRepository;

    public void createAdmin(NewAdminDto newAdminDto) {
        Admin admin = modelMapper.map(newAdminDto, Admin.class);
        admin.setRole(Role.ADMIN);
        adminRepository.save(admin);
    }
}
