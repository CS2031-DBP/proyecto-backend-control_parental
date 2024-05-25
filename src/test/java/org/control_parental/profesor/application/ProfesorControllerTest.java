package org.control_parental.padre.application;

import jakarta.transaction.Transactional;
import org.control_parental.hijo.domain.Hijo;
import org.control_parental.hijo.infrastructure.HijoRepository;
import org.control_parental.padre.domain.Padre;
import org.control_parental.padre.dto.NewPadreDto;
import org.control_parental.padre.infrastructure.PadreRepository;
import org.control_parental.profesor.domain.Profesor;
import org.control_parental.profesor.dto.NewProfesorDto;
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

import javax.print.attribute.standard.Media;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class ProfesorControllerTest {

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
        //profesorRepository.save(profesor);
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

        profesor.setPublicaciones(publicaciones);
        salon = new Salon();
        salon.setNombre("Salon1");

        salon.setHijos(hijos);

        salon.setProfesores(profesores);

        salon.setPublicaciones(publicaciones);

    }

    @Test
    public void testSaveProfesor() throws Exception{
        NewProfesorDto newProfesorDto = new NewProfesorDto();
        newProfesorDto.setApellido("Rios");
        newProfesorDto.setNombre("Jorge");
        newProfesorDto.setEmail("jorgerios@utec.edu.pe");
        newProfesorDto.setPassword(passwordEncoder.encode("654321"));


        var test = mockMvc.perform(MockMvcRequestBuilders.post("/profesor")
                .contentType(MediaType.APPLICATION_JSON)
                .contentType(objectMapper.writeValueAsString(newProfesorDto)))
                .andExpect(status().isCreated()).andReturn();

        String location = test.getResponse().getHeader("Location");
        Long id = Long.valueOf(location.substring(location.lastIndexOf("/") + 1));

        Profesor newProfesor = profesorRepository.findById(id).orElseThrow();

        Assertions.assertEquals(salon.getNombre(), newProfesor.getNombre());
    }

    @Test
    public void testGetProfesor() throws Exception{
        profesorRepository.save(profesor);

        mockMvc.perform(MockMvcRequestBuilders.get("/profesor/{id}", profesor.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").doesNotExist())
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(profesor.getEmail()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.nombre").value(profesor.getNombre()))
                .andExpect(status().isOk());
    }

    @Test
    public void testDeleteProfesor() throws Exception{
        Profesor profesor1 = profesorRepository.save(profesor);

        mockMvc.perform(MockMvcRequestBuilders.delete("/profesor/{id}", profesor1.getId()))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testPatchProfesor() throws Exception{
        NewProfesorDto newProfesorDto = new NewProfesorDto();
        newProfesorDto.setApellido("Rios");
        newProfesorDto.setNombre("Jorge");
        newProfesorDto.setEmail("jorgerios@utec.edu.pe");
        newProfesorDto.setPassword(passwordEncoder.encode("654321"));

        Profesor profesor1 = profesorRepository.save(profesor);

        mockMvc.perform(MockMvcRequestBuilders.patch("profesor/{id}", profesor1.getId()))
                .andExpect(status().isOk());

        Profesor profesor2 = profesorRepository.findById(profesor1.getId()).orElseThrow();
        Assertions.assertEquals(profesor2.getApellido(), newProfesorDto.getApellido());
        Assertions.assertEquals(profesor2.getEmail(), newProfesorDto.getEmail());
        Assertions.assertEquals(profesor2.getApellido(), newProfesorDto.getNombre());

    }

    @Test
    public void testGetPublicaciones() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/profesor/{id}/publicaciones", profesor.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].descripcion").value(publicacion.getDescripcion()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].titulo").value(salon.getPublicaciones().get(0).getTitulo()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].hijos[0].nombre").value(salon.getPublicaciones().get(0).getHijos().get(0).getNombre()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].hijos[1].nombre").value(salon.getPublicaciones().get(0).getHijos().get(1).getNombre()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].profesor.email").value(salon.getPublicaciones().get(0).getProfesor().getEmail()))
                .andExpect(status().isOk());
    }

    @Test
    void testNewPassword() throws Exception{
        NewPasswordDto newPasswordDto = new NewPasswordDto();
        newPasswordDto.setPassword("2345678");
        newPasswordDto.setEmail("eduardo.aragon@utec.edu.pe");

        var test = mockMvc.perform(MockMvcRequestBuilders.patch("profesor/password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newPasswordDto)))
                .andExpect(status().isOk())
                .andReturn();

        //get my email

    }
}