package org.control_parental;

import org.control_parental.admin.domain.Admin;
import org.control_parental.admin.infrastructure.AdminRepository;
import org.control_parental.nido.Domain.Nido;
import org.control_parental.nido.Infrastructure.NidoRepository;
import org.control_parental.usuario.domain.Role;
import org.control_parental.usuario.domain.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class ControlParentalApplication {
	@Autowired
	PasswordEncoder encoder;

	@Autowired
	AdminRepository adminRepository;

	@Autowired
	NidoRepository nidoRepository;

	public static void main(String[] args) {
		SpringApplication.run(ControlParentalApplication.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
		return args -> {
/*
			Nido nido = new Nido();
			nido.setName("Manos y Pies");
			nidoRepository.save(nido);

			Admin admin = new Admin();
			admin.setPassword(encoder.encode("password"));
			admin.setNido(nido);
			admin.setEmail("admin@email.com");
			admin.setRole(Role.ADMIN);
			admin.setNombre("Admin");
			admin.setApellido("Admin");
			adminRepository.save(admin);

			nido.setAdmin(admin);
			nidoRepository.save(nido);
*/

		};
	}
}
