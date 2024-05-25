package org.control_parental.padre.application;

import jakarta.transaction.Transactional;
import org.control_parental.hijo.domain.Hijo;
import org.control_parental.hijo.infrastructure.HijoRepository;
import org.control_parental.padre.domain.Padre;
import org.control_parental.padre.dto.NewPadreDto;
import org.control_parental.padre.infrastructure.PadreRepository;
import org.control_parental.profesor.domain.Profesor;
import org.control_parental.profesor.infrastructure.ProfesorRepository;
import org.control_parental.publicacion.domain.Publicacion;
import org.control_parental.publicacion.infrastructure.PublicacionRepository;
import org.control_parental.salon.domain.Salon;
import org.control_parental.salon.infrastructure.SalonRepository;
import org.control_parental.usuario.NewPasswordDto;
import org.control_parental.usuario.domain.Role;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class PadreControllerTest {
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
    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    public void setUp() throws Exception{
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
    @WithMockUser(roles={"ADMIN"})
    public void TestSavePadre() throws Exception{
        NewPadreDto newPadreDto = new NewPadreDto();
        newPadreDto.setNombre("Jorge");
        newPadreDto.setApellido("Rios");
        newPadreDto.setEmail("jorge.rios@utec.edu.pe");
        newPadreDto.setPhoneNumber("987654321");
        newPadreDto.setPassword("DBPfiltro");

        var test = mockMvc.perform(MockMvcRequestBuilders.post("/padre")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newPadreDto)))
                .andExpect(status().isCreated())
        .andReturn();

        String location = test.getResponse().getHeader("Location");
        Long id = Long.valueOf(location.substring(location.lastIndexOf("/") + 1));

        Padre padre = padreRepository.findById(id).orElseThrow();
        Assertions.assertEquals(padre.getNombre(), newPadreDto.getNombre());
    }

    @Test
    @WithMockUser(roles={"ADMIN", "PROFESOR"})
    void TestGetPadreById() throws Exception{

        Long id = padre1.getId();
        List<Hijo> hijos1 = new ArrayList<Hijo>();
        hijos1.add(hijo1);

        padre1.setHijos(hijos1);
        padreRepository.save(padre1);
/*
        List<Hijo> hijos = new ArrayList<>();
        hijos.add(hijo1);
        hijos.add(hijo2);

        padre1.setHijos(hijos);
        padreRepository.save(padre1);*/

        mockMvc.perform(MockMvcRequestBuilders.get("/padre/{id}", id).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").doesNotExist())
                .andExpect(MockMvcResultMatchers.jsonPath("$.nombre").value(padre1.getNombre()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.hijos[0].nombre").value(hijo1.getNombre()))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles={"ADMIN", "PROFESOR"})
    public void testGetNonPadreExists() throws Exception {

        padreRepository.save(padre1);

        mockMvc.perform(MockMvcRequestBuilders.get("/padre/{id}", 20L).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound()).andReturn();

    }

    @Test
    @WithMockUser(value = "laura.nagamine@utec.edu.pe", roles = {"PADRE"})
    public void testGetMePadre() throws Exception{

        List<Hijo> hijos1 = new ArrayList<>();
        hijos1.add(hijo1);

        padre1.setHijos(hijos1);
        padreRepository.save(padre1);
/*
        List<Hijo> hijos = new ArrayList<>();
        hijos.add(hijo1);
        hijos.add(hijo2);

        padre1.setHijos(hijos);
        padreRepository.save(padre1);*/

        mockMvc.perform(MockMvcRequestBuilders.get("/padre/me").contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").doesNotExist())
                .andExpect(MockMvcResultMatchers.jsonPath("$.nombre").value(padre1.getNombre()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.hijos[0].nombre").value(hijo1.getNombre()))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles={"ADMIN"})
    void testDeletePadre() throws Exception {
        Padre padre11 = padreRepository.save(padre1);
        Long id = padre11.getId();

        mockMvc.perform(MockMvcRequestBuilders.delete("/padre/{id}", id).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

    }

    @Test
    @WithMockUser(roles={"PROFESOR"})
    void TestGetHijos() throws Exception{

        List<Hijo> hijos = new ArrayList<>();
        hijos.add(hijo1);
        hijos.add(hijo2);

        padre1.setHijos(hijos);
        padreRepository.save(padre1);
        Long id = padre1.getId();

        mockMvc.perform(MockMvcRequestBuilders.get("/padre/{id}/hijos", id)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].nombre").value(hijo1.getNombre()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[1].nombre").value(hijo2.getNombre()))
                .andExpect(status().isOk());

    }

    @Test
    void testGetMyHijos() {

    }

    @Test
    @WithMockUser(value = "laura.nagamine@utec.edu.pe", roles = "PADRE")
    void testNewPassword() throws Exception{
        NewPasswordDto newPasswordDto = new NewPasswordDto();
        newPasswordDto.setPassword("2345678");
        newPasswordDto.setEmail("laura.nagamine@utec.edu.pe");

        profesorRepository.save(profesor);

        mockMvc.perform(MockMvcRequestBuilders.patch("/padre/password")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newPasswordDto)))
                .andExpect(status().isOk())
                .andReturn();

        Padre padre12 = padreRepository.findByEmail(newPasswordDto.getEmail()).orElseThrow();
        Assertions.assertEquals(padre12.getPassword(), passwordEncoder.encode(newPasswordDto.getPassword()));

    }
}