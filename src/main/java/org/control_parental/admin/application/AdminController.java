package org.control_parental.admin.application;

import org.control_parental.admin.domain.AdminService;
import org.control_parental.admin.dto.NewAdminDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private AdminService adminService;



    @PostMapping
    public ResponseEntity<Void> createAdmin(@RequestBody NewAdminDto newAdminDto) {
        adminService.createAdmin(newAdminDto);
        return ResponseEntity.created(null).build();
    }
}
