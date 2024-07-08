//package org.control_parental.salon.application;
//
//import jakarta.transaction.Transactional;
//import org.control_parental.auth.dto.AuthLoginRequest;
//import org.control_parental.hijo.domain.Hijo;
//import org.control_parental.hijo.infrastructure.HijoRepository;
//import org.control_parental.padre.domain.Padre;
//import org.control_parental.padre.infrastructure.PadreRepository;
//import org.control_parental.profesor.domain.Profesor;
//import org.control_parental.profesor.infrastructure.ProfesorRepository;
//import org.control_parental.publicacion.domain.Publicacion;
//import org.control_parental.publicacion.infrastructure.PublicacionRepository;
//import org.control_parental.salon.domain.Salon;
//import org.control_parental.salon.dto.NewSalonDTO;
//import org.control_parental.salon.infrastructure.SalonRepository;
//import org.control_parental.usuario.domain.Role;
//import org.json.JSONObject;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.MediaType;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.test.context.support.WithMockUser;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
//
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Objects;
//
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@SpringBootTest
//@AutoConfigureMockMvc
//@Transactional
//public class SalonControllerIntegrationTest {
//
//    @Autowired
//    MockMvc mockMvc;
//
//    @Autowired
//    SalonRepository salonRepository;
//
//    @Autowired
//    HijoRepository hijoRepository;
//
//    @Autowired
//    ProfesorRepository profesorRepository;
//
//    @Autowired
//    PadreRepository padreRepository;
//
//    @Autowired
//    PublicacionRepository publicacionRepository;
//
//    @Autowired
//    ObjectMapper objectMapper;
//
//    @Autowired
//    PasswordEncoder passwordEncoder;
//
//    Profesor profesor;
//
//    Hijo hijo1;
//
//    Hijo hijo2;
//
//    Padre padre1;
//
//    Padre padre2;
//
//    Salon salon;
//
//    Publicacion publicacion;
//
//    String token;
//
//    @BeforeEach
//    public void setUp() {
//        /*padreRepository.deleteAll();
//        hijoRepository.deleteAll();
//        profesorRepository.deleteAll();
//        salonRepository.deleteAll();*/
//
//        profesor = new Profesor();
//        profesor.setNombre("Renato");
//        profesor.setApellido("García");
//        profesor.setEmail("renato.garcia@utec.edu.pe");
//        profesor.setPassword(passwordEncoder.encode("renato123"));
//        profesor.setRole(Role.PROFESOR);
//        profesorRepository.save(profesor);
//        List<Profesor> profesores = new ArrayList<>();
//        profesores.add(profesor);
//
//        padre1 = new Padre();
//        padre1.setNombre("Laura");
//        padre1.setApellido("Nagamine");
//        padre1.setEmail("laura.nagamine@utec.edu.pe");
//        padre1.setPassword(passwordEncoder.encode("laura123"));
//        padre1.setPhoneNumber("123456789");
//        padre1.setRole(Role.PADRE);
//        padreRepository.save(padre1);
//
//        padre2 = new Padre();
//        padre2.setNombre("Michael");
//        padre2.setApellido("Hinojosa");
//        padre2.setEmail("michael.hinojosa@utec.edu.pe");
//        padre2.setPassword(passwordEncoder.encode("michael123"));
//        padre2.setPhoneNumber("223456789");
//        padre2.setRole(Role.PADRE);
//        padreRepository.save(padre2);
//
//        hijo1 = new Hijo();
//        hijo1.setNombre("Eduardo");
//        hijo1.setApellido("Aragón");
//        hijo1.setPadre(padre1);
//        hijoRepository.save(hijo1);
//
//        hijo2 = new Hijo();
//        hijo2.setNombre("Mikel");
//        hijo2.setApellido("Bracamonte");
//        hijo2.setPadre(padre2);
//        hijoRepository.save(hijo2);
//        List<Hijo> hijos = new ArrayList<>();
//        hijos.add(hijo1);
//        hijos.add(hijo2);
//
//        publicacion = new Publicacion();
//        publicacion.setDescripcion("Esta es una descripción");
//        publicacion.setHijos(hijos);
//        publicacion.setFoto("Esta es una foto");
//        publicacion.setTitulo("Este es un título");
//        publicacion.setFecha(LocalDateTime.now());
//        publicacion.setLikes(0);
//        publicacion.setProfesor(profesor);
//        publicacionRepository.save(publicacion);
//        List<Publicacion> publicaciones = new ArrayList<>();
//        publicaciones.add(publicacion);
//
//        salon = new Salon();
//        salon.setNombre("Salón 101");
//
//        salon.setHijos(hijos);
//
//        salon.setProfesores(profesores);
//
//        salon.setPublicaciones(publicaciones);
//    }
//
//    public void logIn() throws Exception{
//        AuthLoginRequest authLoginRequest = new AuthLoginRequest();
//        authLoginRequest.setEmail("renato.garcia@utec.edu.pe");
//        authLoginRequest.setPassword("renato123");
//
//        var test = mockMvc.perform(MockMvcRequestBuilders.post("/auth/login")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(authLoginRequest)))
//                .andExpect(status().isOk()).andReturn();
//
//        JSONObject jsonObject = new JSONObject(Objects.requireNonNull(test.getResponse().getContentAsString()));
//        token = jsonObject.getString("token");
//    }
//
//    @Test
//    @WithMockUser(roles={"ADMIN"})
//    public void testCreateSalon() throws Exception {
//
//        NewSalonDTO salonData = new NewSalonDTO();
//        salonData.setNombre("Salón 101");
//
//        var test = mockMvc.perform(MockMvcRequestBuilders.post("/salon")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(salonData)))
//                .andExpect(status().isCreated()
//                ).andReturn();
//
//        String location = test.getResponse().getHeader("Location");
//        Long id = Long.valueOf(location.substring(location.lastIndexOf("/") + 1));
//
//        Salon newSalon = salonRepository.findById(id).orElseThrow();
//
//        Assertions.assertEquals("Salón 101", newSalon.getNombre());
//    }
//
//    @Test
//    public void testUnauthorizedCreateSalon() throws Exception {
//        NewSalonDTO salonData = new NewSalonDTO();
//        salonData.setNombre("Salón 101");
//
//        mockMvc.perform(MockMvcRequestBuilders.post("/salon")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(salonData)))
//                .andExpect(status().isForbidden());
//    }
//
//    @Test
//    public void testGetSalon() throws Exception {
//
//        salonRepository.save(salon);
//
//        logIn();
//
//        mockMvc.perform(MockMvcRequestBuilders.get("/salon/{id}", salon.getId())
//                        .header("Authorization", "Bearer " + token))
//                .andExpect(jsonPath("$.nombre").value("Salón 101"))
//                .andExpect(jsonPath("$.hijos[0].nombre").value("Eduardo"))
//                .andExpect(jsonPath("$.hijos[1].nombre").value("Mikel"))
//                .andExpect(jsonPath("$.profesores[0].email").value("renato.garcia@utec.edu.pe"))
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    public void testGetNonexistentSalon() throws Exception {
//
//        salonRepository.save(salon);
//
//        logIn();
//
//        mockMvc.perform(MockMvcRequestBuilders.get("/salon/{id}", 20L)
//                        .header("Authorization", "Bearer " + token)
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isNotFound());
//    }
//
//    @Test
//    public void testUnauthorizedGetSalon() throws Exception{
//
//        salonRepository.save(salon);
//
//        mockMvc.perform(MockMvcRequestBuilders.get("/salon/{id}", salon.getId()))
//                .andExpect(status().isForbidden());
//
//    }
//
//    @Test
//    @WithMockUser(roles={"ADMIN"})
//    public void testAddHijo() throws Exception {
//        salonRepository.save(salon);
//
//        Hijo hijo3 = new Hijo();
//        hijo3.setNombre("Nicolas");
//        hijo3.setApellido("Stigler");
//        hijo3.setPadre(padre1);
//        hijoRepository.save(hijo3);
//
//        mockMvc.perform(MockMvcRequestBuilders.patch("/salon/{salonId}/hijo/{idHijo}", salon.getId(), hijo3.getId()).contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk());
//
//        Salon newSalon = salonRepository.findById(salon.getId()).orElseThrow();
//
//        Assertions.assertEquals(3, newSalon.getHijos().size());
//        Assertions.assertEquals("Nicolas", newSalon.getHijos().get(2).getNombre());
//        Assertions.assertEquals("Stigler", newSalon.getHijos().get(2).getApellido());
//        Assertions.assertEquals("laura.nagamine@utec.edu.pe", newSalon.getHijos().get(2).getPadre().getEmail());
//
//    }
//
//    @Test
//    @WithMockUser(roles={"ADMIN"})
//    public void testAddNonexistentHijo() throws Exception {
//        salonRepository.save(salon);
//
//        logIn();
//
//        mockMvc.perform(MockMvcRequestBuilders.patch("/salon/{idSalon}/hijo/{idHijo}", salon.getId(), 20L)
//                        .header("Authorization", "Bearer " + token)
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isNotFound());
//    }
//
//    @Test
//    public void testUnauthorizedAddHijo() throws Exception {
//        salonRepository.save(salon);
//
//        mockMvc.perform(MockMvcRequestBuilders.patch("/salon/{idSalon}/hijo/{idHijo}", salon.getId(), 20L)
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isForbidden());
//    }
//
//    @Test
//    public void testGetHijos() throws Exception {
//        salonRepository.save(salon);
//
//        logIn();
//
//        mockMvc.perform(MockMvcRequestBuilders.get("/salon/{id}/hijos", salon.getId())
//                        .header("Authorization", "Bearer " + token)
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$.[0].nombre").value("Eduardo"))
//                .andExpect(jsonPath("$.[0].apellido").value("Aragón"))
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    public void testUnauthorizedGetHijos() throws Exception {
//        salonRepository.save(salon);
//
//        mockMvc.perform(MockMvcRequestBuilders.get("/salon/{id}/hijos", salon.getId())
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isForbidden());
//    }
//
//    @Test
//    public void testGetPublicaciones() throws Exception {
//        salonRepository.save(salon);
//
//        logIn();
//
//        mockMvc.perform(MockMvcRequestBuilders.get("/salon/{id}/publicaciones", salon.getId())
//                        .header("Authorization", "Bearer " + token)
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$.[0].titulo").value("Este es un título"))
//                .andExpect(jsonPath("$.[0].hijos[0].nombre").value("Eduardo"))
//                .andExpect(jsonPath("$.[0].hijos[1].nombre").value("Mikel"))
//                .andExpect(jsonPath("$.[0].profesor.email").value("renato.garcia@utec.edu.pe"))
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    public void testUnauthorizedGetPublicaciones() throws Exception {
//        salonRepository.save(salon);
//
//        mockMvc.perform(MockMvcRequestBuilders.get("/salon/{id}/publicaciones", salon.getId())
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isForbidden());
//    }
//
//    @Test
//    @WithMockUser(roles={"ADMIN"})
//    public void testAddProfesor() throws Exception {
//        salonRepository.save(salon);
//
//        Profesor profesor2 = new Profesor();
//        profesor2.setNombre("Gino");
//        profesor2.setApellido("Daza");
//        profesor2.setEmail("gino.daza@utec.edu.pe");
//        profesor2.setPassword("gino123");
//        profesor2.setRole(Role.PROFESOR);
//        profesorRepository.save(profesor2);
//
//        mockMvc.perform(MockMvcRequestBuilders.patch("/salon/{salonId}/profesor/{idProfesor}", salon.getId(), profesor2.getId()).contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk());
//
//        Salon newSalon = salonRepository.findById(salon.getId()).orElseThrow();
//
//        Assertions.assertEquals(2, newSalon.getProfesores().size());
//        Assertions.assertEquals("Gino", newSalon.getProfesores().get(1).getNombre());
//        Assertions.assertEquals("Daza", newSalon.getProfesores().get(1).getApellido());
//        Assertions.assertEquals("gino.daza@utec.edu.pe", newSalon.getProfesores().get(1).getEmail());
//    }
//
//    @Test
//    @WithMockUser(roles={"ADMIN"})
//    public void testAddNonexistentProfesor() throws Exception {
//        salonRepository.save(salon);
//
//        logIn();
//
//        mockMvc.perform(MockMvcRequestBuilders.patch("/salon/{idSalon}/profesor/{idProfesor}", salon.getId(), 20L)
//                        .header("Authorization", "Bearer " + token)
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isNotFound());
//    }
//
//    @Test
//    public void testUnauthorizedAddProfesor() throws Exception {
//        salonRepository.save(salon);
//
//        mockMvc.perform(MockMvcRequestBuilders.patch("/salon/{idSalon}/profesor/{idProfesor}", salon.getId(), 20L)
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isForbidden());
//    }
//}