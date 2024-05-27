package org.control_parental.comentario.application;

import jakarta.transaction.Transactional;
import org.control_parental.auth.dto.AuthLoginRequest;
import org.control_parental.comentario.domain.Comentario;
import org.control_parental.comentario.dto.NewComentarioDto;
import org.control_parental.comentario.infrastructure.ComentarioRepository;
import org.control_parental.hijo.domain.Hijo;
import org.control_parental.hijo.infrastructure.HijoRepository;
import org.control_parental.padre.domain.Padre;
import org.control_parental.padre.infrastructure.PadreRepository;
import org.control_parental.profesor.domain.Profesor;
import org.control_parental.profesor.infrastructure.ProfesorRepository;
import org.control_parental.publicacion.domain.Publicacion;
import org.control_parental.publicacion.infrastructure.PublicacionRepository;
import org.control_parental.salon.domain.Salon;
import org.control_parental.salon.infrastructure.SalonRepository;
import org.control_parental.usuario.domain.Role;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ComentarioControllerIntegrationTest {

    LocalDateTime localDateTime;

    Comentario comentario;

    Padre padre;

    Publicacion publicacion;

    Hijo hijo1;

    Hijo hijo2;

    Profesor profesor;

    Salon salon;

    @Autowired
    ComentarioRepository comentarioRepository;

    @Autowired
    PadreRepository padreRepository;

    @Autowired
    PublicacionRepository publicacionRepository;

    @Autowired
    HijoRepository hijoRepository;

    @Autowired
    ProfesorRepository profesorRepository;

    @Autowired
    SalonRepository salonRepository;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    MockMvc mockMvc;

    String token;

    @BeforeEach
    public void setUp() {

        profesor = new Profesor();
        profesor.setNombre("Renato");
        profesor.setApellido("García");
        profesor.setEmail("renato.garcia@utec.edu.pe");
        profesor.setPassword(passwordEncoder.encode("renato123"));
        profesor.setRole(Role.PROFESOR);

        List<Profesor> profesores = new ArrayList<>();
        profesores.add(profesor);

        padre = new Padre();
        padre.setNombre("Laura");
        padre.setApellido("Nagamine");
        padre.setEmail("laura.nagamine@utec.edu.pe");
        padre.setPassword(passwordEncoder.encode("laura123"));
        padre.setPhoneNumber("123456789");
        padre.setRole(Role.PADRE);

        hijo1 = new Hijo();
        hijo1.setNombre("Eduardo");
        hijo1.setApellido("Aragón");
        hijo1.setPadre(padre);
        hijoRepository.save(hijo1);

        hijo2 = new Hijo();
        hijo2.setNombre("Mikel");
        hijo2.setApellido("Bracamonte");
        hijo2.setPadre(padre);
        hijoRepository.save(hijo2);
        List<Hijo> hijos = new ArrayList<>();
        hijos.add(hijo1);
        hijos.add(hijo2);
        padre.setHijos(hijos);

        localDateTime = LocalDateTime.now();
        publicacion = new Publicacion();
        publicacion.setDescripcion("Esta es una descripción");
        publicacion.setHijos(hijos);
        publicacion.setFoto("Esta es una foto");
        publicacion.setTitulo("Este es un título");
        publicacion.setFecha(localDateTime);
        publicacion.setLikes(0);
        publicacion.setProfesor(profesor);
        List<Publicacion> publicaciones = new ArrayList<>();
        publicaciones.add(publicacion);

        salon = new Salon();
        salon.setNombre("Salón 101");

        salon.setHijos(hijos);

        salon.setProfesores(profesores);

        salon.setPublicaciones(publicaciones);

        salonRepository.save(salon);
        publicacion.setSalon(salon);
        publicacionRepository.save(publicacion);

        comentario = new Comentario();
        comentario.setPublicacion(publicacion);
        comentario.setFecha(localDateTime);
        comentario.setContenido("Este es un comentario");
        comentario.setUsuario(padre);

        padreRepository.save(padre);
        profesorRepository.save(profesor);

    }

    public void logIn(String email, String password) throws Exception {
        AuthLoginRequest authLoginRequest = new AuthLoginRequest();
        authLoginRequest.setEmail(email);
        authLoginRequest.setPassword(password);

        var test = mockMvc.perform(MockMvcRequestBuilders.post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authLoginRequest)))
                .andExpect(status().isOk()).andReturn();

        JSONObject jsonObject = new JSONObject(Objects.requireNonNull(test.getResponse().getContentAsString()));
        token = jsonObject.getString("token");
    }

    @Test
    public void testGetComentario() throws Exception {
        logIn("laura.nagamine@utec.edu.pe", "laura123");

        comentarioRepository.save(comentario);

        mockMvc.perform(MockMvcRequestBuilders.get("/comentario/{id}", comentario.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token))
                .andExpect(jsonPath("$.contenido").value("Este es un comentario"))
                .andExpect(jsonPath("$.usuario.nombre").value("Laura"))
                .andExpect(jsonPath("$.usuario.apellido").value("Nagamine"))
                .andExpect(jsonPath("$.publicacion.titulo").value("Este es un título"))
                .andExpect(jsonPath("$.publicacion.descripcion").value("Esta es una descripción"))
                .andExpect(jsonPath("$.publicacion.profesor.email").value("renato.garcia@utec.edu.pe"))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetNonexistentComentario() throws Exception {
        logIn("laura.nagamine@utec.edu.pe", "laura123");

        comentarioRepository.save(comentario);

        mockMvc.perform(MockMvcRequestBuilders.get("/comentario/{id}", 20L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token))
               .andExpect(status().isNotFound());
    }

    @Test
    public void testUnauthorizedGetComentario() throws Exception {

        comentarioRepository.save(comentario);

        mockMvc.perform(MockMvcRequestBuilders.get("/comentario/{id}", 20L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    public void testPostComentario() throws Exception {
        logIn("laura.nagamine@utec.edu.pe", "laura123");

        NewComentarioDto newComentarioDto = new NewComentarioDto();
        newComentarioDto.setContenido("Hola");

        var test = mockMvc.perform(MockMvcRequestBuilders.post("/comentario")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newComentarioDto))
                .param("PublicacionId", String.valueOf(publicacion.getId())))
                .andExpect(status().isCreated())
                .andReturn();

        String location = test.getResponse().getHeader("Location");
        Long id = Long.valueOf(location.substring(location.lastIndexOf("/") + 1));

        Comentario newComentario = comentarioRepository.findById(id).orElseThrow();

        Assertions.assertEquals("Hola", newComentario.getContenido());
        Assertions.assertEquals("laura.nagamine@utec.edu.pe", newComentario.getUsuario().getEmail());
        Assertions.assertEquals("Esta es una descripción", newComentario.getPublicacion().getDescripcion());
        Assertions.assertEquals("renato.garcia@utec.edu.pe", newComentario.getPublicacion().getProfesor().getEmail());
    }

    @Test
    public void testPostComentarioOnNonexistentPublicacion() throws Exception {
        logIn("laura.nagamine@utec.edu.pe", "laura123");

        NewComentarioDto newComentarioDto = new NewComentarioDto();
        newComentarioDto.setContenido("Hola");

        mockMvc.perform(MockMvcRequestBuilders.post("/comentario")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newComentarioDto))
                        .param("PublicacionId", String.valueOf(20L)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testUnauthorizedPostComentario() throws Exception {

        NewComentarioDto newComentarioDto = new NewComentarioDto();
        newComentarioDto.setContenido("Hola");

        mockMvc.perform(MockMvcRequestBuilders.post("/comentario")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newComentarioDto))
                        .param("PublicacionId", String.valueOf(publicacion.getId())))
                .andExpect(status().isForbidden());
    }

    @Test
    public void testPatchComentario() throws Exception {
        logIn("laura.nagamine@utec.edu.pe", "laura123");

        comentarioRepository.save(comentario);

        NewComentarioDto newComentarioDto = new NewComentarioDto();
        newComentarioDto.setContenido("Hola");

        mockMvc.perform(MockMvcRequestBuilders.patch("/comentario/{id}", comentario.getId())
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newComentarioDto)))
                .andExpect(status().isOk());

        Comentario newComentario = comentarioRepository.findById(comentario.getId()).orElseThrow();

        Assertions.assertEquals("Hola", newComentario.getContenido());
        Assertions.assertEquals("laura.nagamine@utec.edu.pe", newComentario.getUsuario().getEmail());
        Assertions.assertEquals("Esta es una descripción", newComentario.getPublicacion().getDescripcion());
        Assertions.assertEquals("renato.garcia@utec.edu.pe", newComentario.getPublicacion().getProfesor().getEmail());

    }

    @Test
    public void testPatchNonexistentComentario() throws Exception {
        logIn("laura.nagamine@utec.edu.pe", "laura123");

        comentarioRepository.save(comentario);

        NewComentarioDto newComentarioDto = new NewComentarioDto();
        newComentarioDto.setContenido("Hola");

        mockMvc.perform(MockMvcRequestBuilders.patch("/comentario/{id}", 20L)
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newComentarioDto)))
                .andExpect(status().isNotFound());

    }

    @Test
    public void testUnauthorizedPatchComentario() throws Exception {
        Padre padre2 = new Padre();
        padre2.setRole(Role.PADRE);
        padre2.setNombre("Michael");
        padre2.setApellido("Hinojosa");
        padre2.setPhoneNumber("132546576");
        padre2.setEmail("michael.hinojosa@utec.edu.pe");
        padre2.setPassword(passwordEncoder.encode("michael123"));
        padreRepository.save(padre2);

        logIn("michael.hinojosa@utec.edu.pe", "michael123");

        comentarioRepository.save(comentario);

        NewComentarioDto newComentarioDto = new NewComentarioDto();
        newComentarioDto.setContenido("Hola");

        mockMvc.perform(MockMvcRequestBuilders.patch("/comentario/{id}", comentario.getId())
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newComentarioDto)))
                .andExpect(status().isForbidden());
    }

    @Test
    public void testDeleteComentario() throws Exception {
        logIn("laura.nagamine@utec.edu.pe", "laura123");

        comentarioRepository.save(comentario);

        Long id = comentario.getId();

        mockMvc.perform(MockMvcRequestBuilders.delete("/comentario/{id}", comentario.getId())
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        Assertions.assertFalse(comentarioRepository.existsById(id));
    }

    @Test
    public void testDeleteNonexistentComentario() throws Exception {
        logIn("laura.nagamine@utec.edu.pe", "laura123");

        comentarioRepository.save(comentario);

        mockMvc.perform(MockMvcRequestBuilders.delete("/comentario/{id}", 20L)
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testUnauthorizedDeleteComentario() throws Exception {
        Padre padre2 = new Padre();
        padre2.setRole(Role.PADRE);
        padre2.setNombre("Michael");
        padre2.setApellido("Hinojosa");
        padre2.setPhoneNumber("132546576");
        padre2.setEmail("michael.hinojosa@utec.edu.pe");
        padre2.setPassword(passwordEncoder.encode("michael123"));
        padreRepository.save(padre2);

        logIn("michael.hinojosa@utec.edu.pe", "michael123");

        comentarioRepository.save(comentario);

        mockMvc.perform(MockMvcRequestBuilders.delete("/comentario/{id}", comentario.getId())
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }
}
