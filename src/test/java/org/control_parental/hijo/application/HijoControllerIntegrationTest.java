package org.control_parental.hijo.application;

import jakarta.transaction.Transactional;
import org.control_parental.hijo.domain.Hijo;
import org.control_parental.hijo.infrastructure.HijoRepository;
import org.control_parental.padre.domain.Padre;
import org.control_parental.padre.infrastructure.PadreRepository;
import org.control_parental.publicacion.domain.Publicacion;
import org.control_parental.publicacion.infrastructure.PublicacionRepository;
import org.control_parental.salon.domain.Salon;
import org.control_parental.salon.infrastructure.SalonRepository;
import org.control_parental.usuario.domain.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.parameters.P;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class HijoControllerIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    HijoRepository hijoRepository;

    @Autowired
    PadreRepository padreRepository;

    @Autowired
    SalonRepository salonRepository;

    @Autowired
    PublicacionRepository publicacionRepository;

    @Autowired
    ModelMapper modelMapper;

    Hijo hijo;

    Padre padre;

    Salon salon;

    Publicacion publicacion1;

    Publicacion publicacion2;

    @BeforeEach
    public void setUp() {
        padre = new Padre();
        padre.setNombre("Michael");
        padre.setApellido("Hinojosa");
        padre.setRole(Role.PADRE);
        padre.setPhoneNumber("123456789");
        padre.setEmail("michael.hinojosa@utec.edu.pe");
        padre.setPassword("123456");
        padreRepository.save(padre);

        salon = new Salon();
        salon.setNombre("Salon 101");
        salonRepository.save(salon);

        publicacion1 = new Publicacion();
        publicacion1.setDescripcion("Esta es una publicación");
        publicacion1.setFoto("Esta es una foto");
        publicacion1.setTitulo("Este es un titulo");
        publicacion1.setFecha(LocalDateTime.now());
        publicacion1.setLikes(0);
        publicacionRepository.save(publicacion1);
        publicacion2 = new Publicacion();
        publicacion2.setDescripcion("Esta es una publicación");
        publicacion2.setFoto("Esta es una foto");
        publicacion2.setTitulo("Este es un titulo");
        publicacion2.setFecha(LocalDateTime.now());
        publicacion2.setLikes(0);
        publicacionRepository.save(publicacion2);
        List<Publicacion> publicaciones = new ArrayList<>();
        publicaciones.add(publicacion1);
        publicaciones.add(publicacion2);

        hijo = new Hijo();
        hijo.setNombre("Eduardo");
        hijo.setApellido("Aragon");
        hijo.setPublicaciones(publicaciones);
        hijo.setSalon(salon);
        hijo.setPadre(padre);
    }

    @Test
    public void testCreateHijo() throws Exception {
        
    }

}
