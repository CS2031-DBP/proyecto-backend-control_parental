package org.control_parental.hijo.application;

import jakarta.transaction.Transactional;
import org.control_parental.auth.dto.AuthLoginRequest;
import org.control_parental.hijo.domain.Hijo;
import org.control_parental.hijo.dto.NewHijoDto;
import org.control_parental.hijo.infrastructure.HijoRepository;
import org.control_parental.padre.domain.Padre;
import org.control_parental.padre.infrastructure.PadreRepository;
import org.control_parental.publicacion.domain.Publicacion;
import org.control_parental.publicacion.infrastructure.PublicacionRepository;
import org.control_parental.salon.domain.Salon;
import org.control_parental.salon.infrastructure.SalonRepository;
import org.control_parental.usuario.domain.Role;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
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

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    PasswordEncoder passwordEncoder;

    Hijo hijo;

    Padre padre;

    Salon salon;

    Publicacion publicacion1;

    Publicacion publicacion2;

    String token;

    @BeforeEach
    public void setUp() {
        padre = new Padre();
        padre.setNombre("Michael");
        padre.setApellido("Hinojosa");
        padre.setRole(Role.PADRE);
        padre.setPhoneNumber("123456789");
        padre.setEmail("michael.hinojosa@utec.edu.pe");
        padre.setPassword(passwordEncoder.encode("123456"));
        padreRepository.save(padre);

        salon = new Salon();
        salon.setNombre("Salón 101");
        salonRepository.save(salon);

        publicacion1 = new Publicacion();
        publicacion1.setDescripcion("Esta es una descripción 1");
        publicacion1.setFoto("Esta es una foto 1");
        publicacion1.setTitulo("Este es un título 1");
        publicacion1.setFecha(LocalDateTime.now());
        publicacion1.setLikes(0);
        publicacionRepository.save(publicacion1);
        publicacion2 = new Publicacion();
        publicacion2.setDescripcion("Esta es una descripción 2");
        publicacion2.setFoto("Esta es una foto 2");
        publicacion2.setTitulo("Este es un título 2");
        publicacion2.setFecha(LocalDateTime.now());
        publicacion2.setLikes(0);
        publicacionRepository.save(publicacion2);
        List<Publicacion> publicaciones = new ArrayList<>();
        publicaciones.add(publicacion1);
        publicaciones.add(publicacion2);

        hijo = new Hijo();
        hijo.setNombre("Eduardo");
        hijo.setApellido("Aragón");
        hijo.setPublicaciones(publicaciones);
        hijo.setSalon(salon);
        hijo.setPadre(padre);
    }

    public String logIn() throws Exception{
        AuthLoginRequest authLoginRequest = new AuthLoginRequest();
        authLoginRequest.setEmail("michael.hinojosa@utec.edu.pe");
        authLoginRequest.setPassword("123456");

        var test = mockMvc.perform(MockMvcRequestBuilders.post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authLoginRequest)))
                .andExpect(status().isOk()).andReturn();

        JSONObject jsonObject = new JSONObject(Objects.requireNonNull(test.getResponse().getContentAsString()));
        token = jsonObject.getString("token");
        return token;
    }

    @Test
    public void testGetStudentById() throws Exception {
        hijoRepository.save(hijo);

        token = logIn();

        mockMvc.perform(MockMvcRequestBuilders.get("/hijo/{id}", hijo.getId())
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.nombre").value("Eduardo"))
                .andExpect(jsonPath("$.apellido").value("Aragón"))
                .andExpect(jsonPath("$.padre.email").value("michael.hinojosa@utec.edu.pe"))
                .andExpect(jsonPath("$.publicaciones[0].titulo").value("Este es un título 1"))
                .andExpect(jsonPath("$.publicaciones[1].titulo").value("Este es un título 2"))
                .andExpect(jsonPath("$.salon.nombre").value("Salón 101"))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetNonexistentStudent() throws Exception {

        token = logIn();

        mockMvc.perform(MockMvcRequestBuilders.get("/hijo/{id}", 20L)
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testUnauthorizedGetStudentById() throws Exception {
        hijoRepository.save(hijo);

        mockMvc.perform(MockMvcRequestBuilders.get("/hijo/{id}", hijo.getId())
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles={"ADMIN"})
    public void testCreateStudent() throws Exception {
        NewHijoDto newHijoData = new NewHijoDto(hijo.getNombre(), hijo.getApellido(), hijo.getPadre().getEmail());

        var test = mockMvc.perform(MockMvcRequestBuilders.post("/hijo")
                .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newHijoData))
                        .param("idPadre", String.valueOf(padre.getId())))
                .andExpect(status().isCreated()).andReturn();

        String location = test.getResponse().getHeader("Location");
        Long id = Long.valueOf(location.substring(location.lastIndexOf("/") + 1));

        Hijo newHijo = hijoRepository.findById(id).orElseThrow();

        Assertions.assertEquals("Eduardo", newHijo.getNombre());
        Assertions.assertEquals("Aragón", newHijo.getApellido());
        Assertions.assertEquals("michael.hinojosa@utec.edu.pe", newHijo.getPadre().getEmail());
    }

    @Test
    @WithMockUser(roles={"ADMIN"})
    public void testCreateStudentWithNonexistentPadre() throws Exception {
        NewHijoDto newHijoData = new NewHijoDto(hijo.getNombre(), hijo.getApellido(), hijo.getPadre().getEmail());

        mockMvc.perform(MockMvcRequestBuilders.post("/hijo")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newHijoData))
                        .param("idPadre", String.valueOf(20)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testUnauthorizedCreateStudent() throws Exception {
        NewHijoDto newHijoData = new NewHijoDto(hijo.getNombre(), hijo.getApellido(), hijo.getPadre().getEmail());

        mockMvc.perform(MockMvcRequestBuilders.post("/hijo")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newHijoData))
                        .param("idPadre", String.valueOf(padre.getId())))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles={"ADMIN"})
    public void testDeleteStudent() throws Exception {
        hijoRepository.save(hijo);
        Long id = hijo.getId();

        mockMvc.perform(MockMvcRequestBuilders.delete("/hijo/{id}", hijo.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        Assertions.assertFalse(hijoRepository.existsById(id));
    }

    @Test
    @WithMockUser(roles={"ADMIN"})
    public void testDeleteNonexistentStudent() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/hijo/{id}", 20L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testUnauthorizedDeleteStudent() throws Exception {
        hijoRepository.save(hijo);

        mockMvc.perform(MockMvcRequestBuilders.delete("/hijo/{id}", hijo.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    public void testGetPublicaciones() throws Exception {

        token = logIn();

        hijoRepository.save(hijo);

        mockMvc.perform(MockMvcRequestBuilders.get("/hijo/{id}/publicaciones", hijo.getId())
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[0].titulo").value("Este es un título 1"))
                .andExpect(jsonPath("$.[0].descripcion").value("Esta es una descripción 1"))
                .andExpect(jsonPath("$.[1].titulo").value("Este es un título 2"))
                .andExpect(jsonPath("$.[1].descripcion").value("Esta es una descripción 2"))
                .andExpect(status().isOk());
    }

    @Test
    public void testUnauthorizedGetPublicaciones() throws Exception {
        hijoRepository.save(hijo);

        mockMvc.perform(MockMvcRequestBuilders.get("/hijo/{id}/publicaciones", hijo.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles={"ADMIN"})
    public void testUpdateStudent() throws Exception {
        hijoRepository.save(hijo);

        NewHijoDto newHijoData = new NewHijoDto();
        newHijoData.setNombre("Tamy");
        newHijoData.setApellido("Flores");

        mockMvc.perform(MockMvcRequestBuilders.patch("/hijo/{id}", hijo.getId())
                .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newHijoData)))
                .andExpect(status().isOk());

        Hijo newHijo = hijoRepository.findById(hijo.getId()).orElseThrow();

        Assertions.assertEquals("Tamy", newHijo.getNombre());
        Assertions.assertEquals("Flores", newHijo.getApellido());
    }

    @Test
    public void testUnauthorizedUpdateStudent() throws Exception {
        hijoRepository.save(hijo);

        NewHijoDto newHijoData = new NewHijoDto();
        newHijoData.setNombre("Tamy");
        newHijoData.setApellido("Flores");

        mockMvc.perform(MockMvcRequestBuilders.patch("/hijo/{id}", hijo.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newHijoData)))
                .andExpect(status().isForbidden());
    }
}
