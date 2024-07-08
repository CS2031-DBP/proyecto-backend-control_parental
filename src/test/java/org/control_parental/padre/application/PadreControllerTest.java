package org.control_parental.padre.application;

import jakarta.transaction.Transactional;
import org.control_parental.comentario.domain.Comentario;
import org.control_parental.comentario.infrastructure.ComentarioRepository;
import org.control_parental.hijo.domain.Hijo;
import org.control_parental.hijo.infrastructure.HijoRepository;
import org.control_parental.like.Domain.Padre_Like;
import org.control_parental.like.Infrastructure.LikeRepository;
import org.control_parental.padre.domain.Padre;
import org.control_parental.padre.dto.NewPadreDto;
import org.control_parental.padre.infrastructure.PadreRepository;
import org.control_parental.profesor.domain.Profesor;
import org.control_parental.profesor.infrastructure.ProfesorRepository;
import org.control_parental.publicacion.domain.Publicacion;
import org.control_parental.publicacion.infrastructure.PublicacionRepository;
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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
    ComentarioRepository comentarioRepository;

    @Autowired
    LikeRepository likeRepository;

    @Autowired
    ObjectMapper objectMapper;

    Profesor profesor;

    Hijo hijo1;

    Hijo hijo2;

    Padre padre1;

    Comentario comentario;

    Publicacion publicacion;

    Padre_Like padreLike;

    LocalDateTime localDateTime;

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

        hijo1 = new Hijo();
        hijo1.setNombre("Eduardo");
        hijo1.setApellido("Aragon");
        hijoRepository.save(hijo1);

        hijo2 = new Hijo();
        hijo2.setNombre("Mikel");
        hijo2.setApellido("Bracamonte");
        hijoRepository.save(hijo2);
        List<Hijo> hijos = new ArrayList<>();
        hijos.add(hijo1);
        hijos.add(hijo2);
        padre1.setHijos(hijos);

        localDateTime = LocalDateTime.now();

        publicacion = new Publicacion();
        publicacion.setDescripcion("Esta es una descripción");
        publicacion.setHijos(hijos);
        publicacion.setFoto("Esta es una foto");
        publicacion.setTitulo("Este es un titulo");
        publicacion.setFecha(localDateTime);
        publicacion.setLikes(0);
        publicacion.setProfesor(profesor);
        publicacionRepository.save(publicacion);
        List<Publicacion> publicaciones = new ArrayList<>();
        publicaciones.add(publicacion);

        comentario = new Comentario();
        comentario.setFecha(localDateTime);
        comentario.setContenido("Este es un comentario");
        comentario.setPublicacion(publicacion);
        comentarioRepository.save(comentario);

        List<Comentario> comentarios = new ArrayList<>();
        comentarios.add(comentario);

        padre1.setComentarios(comentarios);

        padreLike = new Padre_Like();
        padreLike.setPublicacion(publicacion);
        likeRepository.save(padreLike);

        List<Padre_Like> padreLikes = new ArrayList<>();
        padreLikes.add(padreLike);

        padre1.setPosts_likeados(padreLikes);

    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    public void testSavePadre() throws Exception {
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
        Assertions.assertEquals("Jorge", padre.getNombre());
    }

    @Test
    public void testUnauthorizedSavePadre() throws Exception {
        NewPadreDto newPadreDto = new NewPadreDto();
        newPadreDto.setNombre("Jorge");
        newPadreDto.setApellido("Rios");
        newPadreDto.setEmail("jorge.rios@utec.edu.pe");
        newPadreDto.setPhoneNumber("987654321");
        newPadreDto.setPassword("DBPfiltro");

        mockMvc.perform(MockMvcRequestBuilders.post("/padre")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newPadreDto)))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(value = "laura.nagamine@utec.edu.pe", roles = {"PADRE"})
    public void testGetPadreById() throws Exception {

        padreRepository.save(padre1);

        mockMvc.perform(MockMvcRequestBuilders.get("/padre/{id}", padre1.getId()).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.nombre").value("Laura"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.hijos[0].nombre").value("Eduardo"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.hijos[1].nombre").value("Mikel"))
                .andExpect(status().isOk());
    }

    @Test
    public void testUnauthorizedGetPadreById() throws Exception {

        padreRepository.save(padre1);

        mockMvc.perform(MockMvcRequestBuilders.get("/padre/{id}", padre1.getId()).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = {"ADMIN", "PROFESOR"})
    public void testGetNonPadreExists() throws Exception {

        padreRepository.save(padre1);

        mockMvc.perform(MockMvcRequestBuilders.get("/padre/{id}", 20L).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound()).andReturn();

    }

    @Test
    @WithMockUser(value = "laura.nagamine@utec.edu.pe", roles = {"PADRE"})
    public void testGetMePadre() throws Exception {

        padreRepository.save(padre1);

        mockMvc.perform(MockMvcRequestBuilders.get("/padre/me").contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.nombre").value("Laura"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.hijos[0].nombre").value("Eduardo"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.hijos[1].nombre").value("Mikel"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.comentarios[0].contenido").value("Este es un comentario"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.publicaciones_likeadas[0].descripcion").value("Esta es una descripción"))
                .andExpect(status().isOk());
    }

    @Test
    public void testUnauthorizedGetMePadre() throws Exception {

        padreRepository.save(padre1);

        mockMvc.perform(MockMvcRequestBuilders.get("/padre/me").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void testDeletePadre() throws Exception {
        padreRepository.save(padre1);

        mockMvc.perform(MockMvcRequestBuilders.delete("/padre/{id}", padre1.getId()).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

    }

    @Test
    void testUnauthorizedDeletePadre() throws Exception {
        padreRepository.save(padre1);

        mockMvc.perform(MockMvcRequestBuilders.delete("/padre/{id}", padre1.getId()).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());

    }

    @Test
    @WithMockUser(roles = {"PROFESOR"})
    void testGetHijos() throws Exception {
        padreRepository.save(padre1);

        mockMvc.perform(MockMvcRequestBuilders.get("/padre/{id}/hijos", padre1.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].nombre").value("Eduardo"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[1].nombre").value("Mikel"))
                .andExpect(status().isOk());

    }

    @Test
    void testUnauthorizedGetHijos() throws Exception {
        padreRepository.save(padre1);

        mockMvc.perform(MockMvcRequestBuilders.get("/padre/{id}/hijos", padre1.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());

    }

    @Test
    @WithMockUser(value = "laura.nagamine@utec.edu.pe", roles = "PADRE")
    void testGetMyHijos() throws Exception{
        padreRepository.save(padre1);

        mockMvc.perform(MockMvcRequestBuilders.get("/padre/myhijos"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].nombre").value("Eduardo"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[1].nombre").value("Mikel"))
                .andExpect(status().isOk());
    }

    @Test
    void testUnauthorizedGetMyHijos() throws Exception{
        padreRepository.save(padre1);

        mockMvc.perform(MockMvcRequestBuilders.get("/padre/myhijos"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(value = "laura.nagamine@utec.edu.pe", roles = "PADRE")
    void testNewPassword() throws Exception {
        NewPasswordDto newPasswordDto = new NewPasswordDto();
        newPasswordDto.setPassword("2345678");
        newPasswordDto.setEmail("laura.nagamine@utec.edu.pe");

        padreRepository.save(padre1);

        mockMvc.perform(MockMvcRequestBuilders.patch("/padre/password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newPasswordDto)))
                .andExpect(status().isOk())
                .andReturn();

        Padre padre12 = padreRepository.findByEmail(newPasswordDto.getEmail()).orElseThrow();
        Assertions.assertTrue(passwordEncoder.matches("2345678", padre12.getPassword()));
    }

    @Test
    void testUnauthorizedNewPassword() throws Exception {
        NewPasswordDto newPasswordDto = new NewPasswordDto();
        newPasswordDto.setPassword("2345678");
        newPasswordDto.setEmail("laura.nagamine@utec.edu.pe");

        padreRepository.save(padre1);

        mockMvc.perform(MockMvcRequestBuilders.patch("/padre/password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newPasswordDto)))
                .andExpect(status().isForbidden());
    }
}