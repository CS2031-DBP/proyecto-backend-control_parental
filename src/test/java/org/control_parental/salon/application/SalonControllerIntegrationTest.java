package org.control_parental.salon.application;

import jakarta.transaction.Transactional;
import org.control_parental.hijo.domain.Hijo;
import org.control_parental.hijo.dto.NewHijoDto;
import org.control_parental.hijo.infrastructure.HijoRepository;
import org.control_parental.padre.domain.Padre;
import org.control_parental.padre.infrastructure.PadreRepository;
import org.control_parental.profesor.domain.Profesor;
import org.control_parental.profesor.infrastructure.ProfesorRepository;
import org.control_parental.publicacion.domain.Publicacion;
import org.control_parental.publicacion.infrastructure.PublicacionRepository;
import org.control_parental.salon.domain.Salon;
import org.control_parental.salon.dto.NewSalonDTO;
import org.control_parental.salon.infrastructure.SalonRepository;
import org.control_parental.usuario.domain.Role;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.parameters.P;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.shaded.com.fasterxml.jackson.core.JsonProcessingException;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class SalonControllerIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    SalonRepository salonRepository;

    @Autowired
    HijoRepository hijoRepository;

    @Autowired
    ProfesorRepository profesorRepository;

    @Autowired
    PadreRepository padreRepository;

    @Autowired
    PublicacionRepository publicacionRepository;

    @Autowired
    ObjectMapper objectMapper;

    Profesor profesor;

    Hijo hijo1;

    Hijo hijo2;

    Padre padre1;

    Padre padre2;

    Salon salon;

    Publicacion publicacion;

    @BeforeEach
    public void setUp() {
        /*padreRepository.deleteAll();
        hijoRepository.deleteAll();
        profesorRepository.deleteAll();
        salonRepository.deleteAll();*/

        profesor = new Profesor();
        profesor.setNombre("Renato");
        profesor.setApellido("Garcia");
        profesor.setEmail("renato.garcia@utec.edu.pe");
        profesor.setPassword("renato123");
        profesor.setRole(Role.PROFESOR);
        profesorRepository.save(profesor);
        List<Profesor> profesores = new ArrayList<>();
        profesores.add(profesor);

        padre1 = new Padre();
        padre1.setNombre("Laura");
        padre1.setApellido("Nagamine");
        padre1.setEmail("laura.nagamine@utec.edu.pe");
        padre1.setPassword("laura123");
        padre1.setPhoneNumber("123456789");
        padre1.setRole(Role.PADRE);
        padreRepository.save(padre1);

        padre2 = new Padre();
        padre2.setNombre("Michael");
        padre2.setApellido("Hinojosa");
        padre2.setEmail("michael.hinojosa@utec.edu.pe");
        padre2.setPassword("michael123");
        padre2.setPhoneNumber("223456789");
        padre2.setRole(Role.PADRE);
        padreRepository.save(padre2);

        hijo1 = new Hijo();
        hijo1.setNombre("Eduardo");
        hijo1.setApellido("Aragon");
        hijo1.setPadre(padre1);
        hijoRepository.save(hijo1);

        hijo2 = new Hijo();
        hijo2.setNombre("Mikel");
        hijo2.setApellido("Bracamonte");
        hijo2.setPadre(padre2);
        hijoRepository.save(hijo2);
        List<Hijo> hijos = new ArrayList<>();
        hijos.add(hijo1);
        hijos.add(hijo2);

        publicacion = new Publicacion();
        publicacion.setDescripcion("Esta es una publicación");
        publicacion.setHijos(hijos);
        publicacion.setFoto("Esta es una foto");
        publicacion.setTitulo("Este es un titulo");
        publicacion.setFecha(LocalDateTime.now());
        publicacion.setLikes(0);
        publicacion.setProfesor(profesor);
        publicacionRepository.save(publicacion);
        List<Publicacion> publicaciones = new ArrayList<>();
        publicaciones.add(publicacion);

        salon = new Salon();
        salon.setNombre("Salon1");

        salon.setHijos(hijos);

        salon.setProfesores(profesores);

        salon.setPublicaciones(publicaciones);

    }

    @Test
    public void testCreateSalon() throws Exception {

        NewSalonDTO salonData = new NewSalonDTO();
        salonData.setNombre("Salon1");

        mockMvc.perform(MockMvcRequestBuilders.post("/salon")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(salonData)))
                .andExpect(status().isCreated()
                );

        Salon newSalon = salonRepository.findById(1L).orElseThrow();

        Assertions.assertEquals(salon.getNombre(), newSalon.getNombre());
    }

    @Test
    public void testGetSalon() throws Exception {

        salonRepository.save(salon);

        mockMvc.perform(MockMvcRequestBuilders.get("/salon/{id}", salon.getId()).contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").doesNotExist())
                .andExpect(jsonPath("$.nombre").value(salon.getNombre()))
                .andExpect(jsonPath("$.hijos[0].nombre").value(salon.getHijos().get(0).getNombre()))
                .andExpect(jsonPath("$.hijos[0].padre.email").value(salon.getHijos().get(0).getPadre().getEmail()))
                .andExpect(jsonPath("$.hijos[1].nombre").value(salon.getHijos().get(1).getNombre()))
                .andExpect(jsonPath("$.hijos[1].padre.email").value(salon.getHijos().get(1).getPadre().getEmail()))
                .andExpect(jsonPath("$.profesores[0].email").value(salon.getProfesores().get(0).getEmail()))
                .andExpect(status().isOk());


    }

    @Test
    public void testGetNonexistentSalon() throws Exception {

        salonRepository.save(salon);

        mockMvc.perform(MockMvcRequestBuilders.get("/salon/{id}", 20L).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testAddHijo() throws Exception {
        salonRepository.save(salon);

        Hijo hijo3 = new Hijo();
        hijo3.setNombre("Nicolas");
        hijo3.setApellido("Stigler");
        hijo3.setPadre(padre1);
        hijoRepository.save(hijo3);

        NewHijoDto newHijoData = new NewHijoDto();
        newHijoData.setNombre(hijo3.getNombre());
        newHijoData.setApellido(hijo3.getApellido());

        mockMvc.perform(MockMvcRequestBuilders.patch("/salon/{id}", salon.getId()).contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newHijoData)))
                .andExpect(status().isOk());

        Salon newSalon = salonRepository.findById(salon.getId()).orElseThrow();

        Assertions.assertEquals(newSalon.getHijos().size(), 3);
        Assertions.assertEquals(newSalon.getHijos().get(2).getNombre(), hijo3.getNombre());
        Assertions.assertEquals(newSalon.getHijos().get(2).getApellido(), hijo3.getApellido());
        Assertions.assertEquals(newSalon.getHijos().get(2).getPadre().getId(), hijo3.getPadre().getId());

    }

    @Test
    public void testAddNonexistentHijo() throws Exception{
        salonRepository.save(salon);

        NewHijoDto newHijoData = new NewHijoDto();
        newHijoData.setNombre("Nicolas");
        newHijoData.setApellido("Stigler");

        mockMvc.perform(MockMvcRequestBuilders.patch("/salon/{id}", salon.getId()).contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newHijoData)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testGetHijos() throws Exception {
        salonRepository.save(salon);

        mockMvc.perform(MockMvcRequestBuilders.get("/salon/{id}/hijos", salon.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[0].nombre").value(salon.getHijos().get(0).getNombre()))
                .andExpect(jsonPath("$.[0].apellido").value(salon.getHijos().get(0).getApellido()))
                .andExpect(jsonPath("$.[0].padre.email").value(salon.getHijos().get(0).getPadre().getEmail()))
                .andExpect(jsonPath("$.[1].nombre").value(salon.getHijos().get(1).getNombre()))
                .andExpect(jsonPath("$.[1].apellido").value(salon.getHijos().get(1).getApellido()))
                .andExpect(jsonPath("$.[1].padre.email").value(salon.getHijos().get(1).getPadre().getEmail()))
                .andExpect(status().isOk());
    }
    @Test
    public void testGetPublicaciones() throws Exception {
        salonRepository.save(salon);

        mockMvc.perform(MockMvcRequestBuilders.get("/salon/{id}/publicaciones", salon.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[0].titulo").value(salon.getPublicaciones().get(0).getTitulo()))
                .andExpect(jsonPath("$.[0].hijos[0].nombre").value(salon.getPublicaciones().get(0).getHijos().get(0).getNombre()))
                .andExpect(jsonPath("$.[0].hijos[1].nombre").value(salon.getPublicaciones().get(0).getHijos().get(1).getNombre()))
                .andExpect(jsonPath("$.[0].profesor.email").value(salon.getPublicaciones().get(0).getProfesor().getEmail()))
                .andExpect(status().isOk());
    }
}