package org.control_parental.publicacion.application;

import jakarta.transaction.Transactional;
import org.control_parental.auth.dto.AuthLoginRequest;
import org.control_parental.comentario.domain.Comentario;
import org.control_parental.comentario.infrastructure.ComentarioRepository;
import org.control_parental.hijo.domain.Hijo;
import org.control_parental.hijo.infrastructure.HijoRepository;
import org.control_parental.like.Domain.Padre_Like;
import org.control_parental.like.Infrastructure.LikeRepository;
import org.control_parental.padre.domain.Padre;
import org.control_parental.padre.infrastructure.PadreRepository;
import org.control_parental.profesor.domain.Profesor;
import org.control_parental.profesor.infrastructure.ProfesorRepository;
import org.control_parental.publicacion.domain.Publicacion;
import org.control_parental.publicacion.dto.NewPublicacionDto;
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
public class PublicacionControllerIntegrationTest {

    @Autowired
    ProfesorRepository profesorRepository;

    @Autowired
    PadreRepository padreRepository;

    @Autowired
    HijoRepository hijoRepository;

    @Autowired
    SalonRepository salonRepository;

    @Autowired
    ComentarioRepository comentarioRepository;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    PublicacionRepository publicacionRepository;

    @Autowired
    LikeRepository likeRepository;

    LocalDateTime localDateTime;

    Profesor profesor;

    Padre padre;

    Hijo hijo1;

    Hijo hijo2;

    Salon salon;

    Comentario comentario;

    Publicacion publicacion;

    Padre_Like padreLike;

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

        salon = new Salon();
        salon.setNombre("Salón 101");

        salon.setHijos(hijos);

        salon.setProfesores(profesores);

        salonRepository.save(salon);
        publicacion.setSalon(salon);

        padreLike = new Padre_Like();
        padreLike.setPadre(padre);

        List<Padre_Like> padreLikes = new ArrayList<>();
        padreLikes.add(padreLike);

        publicacion.setLikers(padreLikes);

        likeRepository.save(padreLike);
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
    public void testGetPublicacion() throws Exception {
        comentario = new Comentario();
        comentario.setPublicacion(publicacion);
        comentario.setFecha(localDateTime);
        comentario.setContenido("Este es un comentario");
        comentario.setUsuario(padre);
        List<Comentario> comentarios = new ArrayList<>();
        comentarios.add(comentario);

        publicacion.setComentarios(comentarios);
        comentarioRepository.save(comentario);
        publicacion.setProfesor(profesor);
        publicacionRepository.save(publicacion);

        logIn("renato.garcia@utec.edu.pe", "renato123");

        mockMvc.perform(MockMvcRequestBuilders.get("/publicacion/{id}", publicacion.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token))
                .andExpect(jsonPath("$.descripcion").value("Esta es una descripción"))
                .andExpect(jsonPath("$.likes").value(0))
                .andExpect(jsonPath("$.titulo").value("Este es un título"))
                .andExpect(jsonPath("$.hijos[0].nombre").value("Eduardo"))
                .andExpect(jsonPath("$.hijos[1].nombre").value("Mikel"))
                .andExpect(jsonPath("$.comentarios[0].contenido").value("Este es un comentario"))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetNonexistentPublicacion() throws Exception {
        comentario = new Comentario();
        comentario.setPublicacion(publicacion);
        comentario.setFecha(localDateTime);
        comentario.setContenido("Este es un comentario");
        comentario.setUsuario(padre);
        List<Comentario> comentarios = new ArrayList<>();
        comentarios.add(comentario);

        publicacion.setComentarios(comentarios);
        comentarioRepository.save(comentario);
        publicacion.setProfesor(profesor);
        publicacionRepository.save(publicacion);

        logIn("renato.garcia@utec.edu.pe", "renato123");

        mockMvc.perform(MockMvcRequestBuilders.get("/publicacion/{id}", 20L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testUnauthorizedGetPublicacion() throws Exception {
        comentario = new Comentario();
        comentario.setPublicacion(publicacion);
        comentario.setFecha(localDateTime);
        comentario.setContenido("Este es un comentario");
        comentario.setUsuario(padre);
        List<Comentario> comentarios = new ArrayList<>();
        comentarios.add(comentario);

        publicacion.setComentarios(comentarios);
        comentarioRepository.save(comentario);
        publicacion.setProfesor(profesor);
        publicacionRepository.save(publicacion);

        mockMvc.perform(MockMvcRequestBuilders.get("/publicacion/{id}", comentario.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    public void testPostPublicacion() throws Exception {
        logIn("renato.garcia@utec.edu.pe", "renato123");

        NewPublicacionDto newPublicacionDto = new NewPublicacionDto();
        newPublicacionDto.setTitulo("Este es un título");
        newPublicacionDto.setDescripcion("Esta es una descripción");
        newPublicacionDto.setSalonId(salon.getId());

        List<Long> hijos_ids = new ArrayList<>();
        hijos_ids.add(hijo1.getId());
        hijos_ids.add(hijo2.getId());
        newPublicacionDto.setHijos_id(hijos_ids);

        var test = mockMvc.perform(MockMvcRequestBuilders.post("/publicacion")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newPublicacionDto)))
                .andExpect(status().isCreated())
                .andReturn();

        String location = test.getResponse().getHeader("Location");
        Long id = Long.valueOf(location.substring(location.lastIndexOf("/") + 1));

        Publicacion newPublicacion = publicacionRepository.findById(id).orElseThrow();

        Assertions.assertEquals("Este es un título", newPublicacion.getTitulo());
        Assertions.assertEquals("Salón 101", newPublicacion.getSalon().getNombre());
        Assertions.assertEquals("Eduardo", newPublicacion.getHijos().get(0).getNombre());
        Assertions.assertEquals("Mikel", newPublicacion.getHijos().get(1).getNombre());
        Assertions.assertEquals("renato.garcia@utec.edu.pe", newPublicacion.getProfesor().getEmail());
        Assertions.assertEquals(0, newPublicacion.getLikes());
    }

    @Test
    public void testPostPublicacionOnNonexistentSalon() throws Exception {
        logIn("renato.garcia@utec.edu.pe", "renato123");

        NewPublicacionDto newPublicacionDto = new NewPublicacionDto();
        newPublicacionDto.setTitulo("Este es un título");
        newPublicacionDto.setDescripcion("Esta es una descripción");
        newPublicacionDto.setSalonId(20L);

        List<Long> hijos_ids = new ArrayList<>();
        hijos_ids.add(hijo1.getId());
        hijos_ids.add(hijo2.getId());
        newPublicacionDto.setHijos_id(hijos_ids);

        mockMvc.perform(MockMvcRequestBuilders.post("/publicacion")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newPublicacionDto)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testUnauthorizedPostPublicacion() throws Exception {

        NewPublicacionDto newPublicacionDto = new NewPublicacionDto();
        newPublicacionDto.setTitulo("Este es un título");
        newPublicacionDto.setDescripcion("Esta es una descripción");
        newPublicacionDto.setSalonId(salon.getId());

        List<Long> hijos_ids = new ArrayList<>();
        hijos_ids.add(hijo1.getId());
        hijos_ids.add(hijo2.getId());
        newPublicacionDto.setHijos_id(hijos_ids);

        mockMvc.perform(MockMvcRequestBuilders.post("/publicacion")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newPublicacionDto)))
                .andExpect(status().isForbidden());
    }

    @Test
    public void testDeletePublicacion() throws Exception {
        logIn("renato.garcia@utec.edu.pe", "renato123");
        publicacion.setProfesor(profesor);
        publicacionRepository.save(publicacion);

        Long id = publicacion.getId();

        mockMvc.perform(MockMvcRequestBuilders.delete("/publicacion/{id}", publicacion.getId())
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        Assertions.assertFalse(publicacionRepository.existsById(id));
    }

    @Test
    public void testDeleteNonexistentPublicacion() throws Exception {
        logIn("renato.garcia@utec.edu.pe", "renato123");
        publicacionRepository.save(publicacion);

        mockMvc.perform(MockMvcRequestBuilders.delete("/publicacion/{id}", 20L)
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testUnauthorizedDeletePublicacion() throws Exception {
        publicacionRepository.save(publicacion);

        mockMvc.perform(MockMvcRequestBuilders.delete("/publicacion/{id}", publicacion.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    public void testLike() throws Exception {
        logIn("laura.nagamine@utec.edu.pe", "laura123");
        publicacion.setProfesor(profesor);
        publicacionRepository.save(publicacion);

        Long id = publicacion.getId();

        Assertions.assertEquals(0, publicacion.getLikes());

        mockMvc.perform(MockMvcRequestBuilders.post("/publicacion/like/{postId}", publicacion.getId())
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        Publicacion newPublicacion = publicacionRepository.findById(id).orElseThrow();

        Assertions.assertEquals(1, newPublicacion.getLikes());

        Assertions.assertTrue(likeRepository.existsByPadre_IdAndPublicacion_Id(padre.getId(), publicacion.getId()));
    }

    @Test
    public void testLikeNonexistentPublicacion() throws Exception {
        logIn("laura.nagamine@utec.edu.pe", "laura123");
        publicacion.setProfesor(profesor);
        publicacionRepository.save(publicacion);

        mockMvc.perform(MockMvcRequestBuilders.post("/publicacion/like/{postId}", 20L)
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testUnauthorizedLike() throws Exception {
        publicacion.setProfesor(profesor);
        publicacionRepository.save(publicacion);

        Assertions.assertEquals(0, publicacion.getLikes());

        mockMvc.perform(MockMvcRequestBuilders.post("/publicacion/like/{postId}", publicacion.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    public void testQuitarLike() throws Exception {
        padreLike = new Padre_Like();
        padreLike.setPadre(padre);

        List<Padre_Like> padreLikes = new ArrayList<>();
        padreLikes.add(padreLike);

        publicacion.setLikers(padreLikes);
        publicacion.setLikes(1);

        logIn("laura.nagamine@utec.edu.pe", "laura123");
        publicacion.setProfesor(profesor);
        publicacionRepository.save(publicacion);
        padreLike.setPublicacion(publicacion);
        likeRepository.save(padreLike);

        Long id = publicacion.getId();

        Assertions.assertEquals(1, publicacion.getLikes());

        mockMvc.perform(MockMvcRequestBuilders.delete("/publicacion/like/{postId}", publicacion.getId())
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        Publicacion newPublicacion = publicacionRepository.findById(id).orElseThrow();

        Assertions.assertEquals(0, newPublicacion.getLikes());

        Assertions.assertFalse(likeRepository.existsByPadre_IdAndPublicacion_Id(padre.getId(), publicacion.getId()));

    }

    @Test
    public void testQuitarLikeNonexistentPublicacion() throws Exception {
        padreLike = new Padre_Like();
        padreLike.setPadre(padre);

        List<Padre_Like> padreLikes = new ArrayList<>();
        padreLikes.add(padreLike);

        publicacion.setLikers(padreLikes);
        publicacion.setLikes(1);

        logIn("laura.nagamine@utec.edu.pe", "laura123");
        publicacion.setProfesor(profesor);
        publicacionRepository.save(publicacion);
        padreLike.setPublicacion(publicacion);
        likeRepository.save(padreLike);

        Assertions.assertEquals(1, publicacion.getLikes());

        mockMvc.perform(MockMvcRequestBuilders.delete("/publicacion/like/{postId}", 20L)
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testUnauthorizedQuitarLike() throws Exception {
        padreLike = new Padre_Like();
        padreLike.setPadre(padre);

        List<Padre_Like> padreLikes = new ArrayList<>();
        padreLikes.add(padreLike);

        publicacion.setLikers(padreLikes);
        publicacion.setLikes(1);

        publicacion.setProfesor(profesor);
        publicacionRepository.save(publicacion);
        padreLike.setPublicacion(publicacion);
        likeRepository.save(padreLike);

        Assertions.assertEquals(1, publicacion.getLikes());

        mockMvc.perform(MockMvcRequestBuilders.delete("/publicacion/like/{postId}", publicacion.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }
}
