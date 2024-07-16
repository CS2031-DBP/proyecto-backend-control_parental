//package org.control_parental.profesor.application;
//
//import jakarta.transaction.Transactional;
//import org.control_parental.comentario.domain.Comentario;
//import org.control_parental.comentario.infrastructure.ComentarioRepository;
//import org.control_parental.hijo.domain.Hijo;
//import org.control_parental.hijo.infrastructure.HijoRepository;
//import org.control_parental.padre.domain.Padre;
//import org.control_parental.padre.infrastructure.PadreRepository;
//import org.control_parental.profesor.domain.Profesor;
//import org.control_parental.profesor.dto.NewProfesorDto;
//import org.control_parental.profesor.infrastructure.ProfesorRepository;
//import org.control_parental.publicacion.domain.Publicacion;
//import org.control_parental.publicacion.infrastructure.PublicacionRepository;
//import org.control_parental.salon.domain.Salon;
//import org.control_parental.salon.infrastructure.SalonRepository;
//import org.control_parental.usuario.NewPasswordDto;
//import org.control_parental.usuario.domain.Role;
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
//import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
//import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
//
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.List;
//
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@SpringBootTest
//@AutoConfigureMockMvc
//@Transactional
//class ProfesorControllerTest {
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
//    ComentarioRepository comentarioRepository;
//
//    @Autowired
//    ObjectMapper objectMapper;
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
//    Comentario comentario;
//
//    LocalDateTime localDateTime;
//
//    @Autowired
//    private PasswordEncoder passwordEncoder;
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
//        profesor.setApellido("Garcia");
//        profesor.setEmail("renato.garcia@utec.edu.pe");
//        profesor.setPassword(passwordEncoder.encode("renato123"));
//        profesor.setRole(Role.PROFESOR);
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
//        hijo1.setApellido("Aragon");
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
//        localDateTime = LocalDateTime.now();
//
//        publicacion = new Publicacion();
//        publicacion.setDescripcion("Esta es una descripción");
//        publicacion.setHijos(hijos);
//        publicacion.setFoto("Esta es una foto");
//        publicacion.setTitulo("Este es un título");
//        publicacion.setFecha(localDateTime);
//        publicacion.setLikes(0);
//        publicacionRepository.save(publicacion);
//        List<Publicacion> publicaciones = new ArrayList<>();
//        publicaciones.add(publicacion);
//
//        profesor.setPublicaciones(publicaciones);
//
//        salon = new Salon();
//        salon.setNombre("Salon1");
//
//        salon.setHijos(hijos);
//
//        salon.setPublicaciones(publicaciones);
//
//        salonRepository.save(salon);
//
//        List<Salon> salones = new ArrayList<>();
//        salones.add(salon);
//
//        profesor.setSalones(salones);
//
//        comentario = new Comentario();
//        comentario.setFecha(localDateTime);
//        comentario.setContenido("Este es un comentario");
//        comentario.setPublicacion(publicacion);
//        comentarioRepository.save(comentario);
//
//        List<Comentario> comentarios = new ArrayList<>();
//        comentarios.add(comentario);
//
//        profesor.setComentarios(comentarios);
//
//    }
//
//    @Test
//    @WithMockUser(roles = {"ADMIN"})
//    public void testSaveProfesor() throws Exception{
//        NewProfesorDto newProfesorDto = new NewProfesorDto();
//        newProfesorDto.setApellido("Rios");
//        newProfesorDto.setNombre("Jorge");
//        newProfesorDto.setEmail("jorgerios@utec.edu.pe");
//        newProfesorDto.setPassword("DBPfiltro");
//
//
//        var test = mockMvc.perform(MockMvcRequestBuilders.post("/profesor")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(objectMapper.writeValueAsString(newProfesorDto)))
//                .andExpect(status().isCreated()).andReturn();
//
//        String location = test.getResponse().getHeader("Location");
//        Long id = Long.valueOf(location.substring(location.lastIndexOf("/") + 1));
//
//        Profesor newProfesor = profesorRepository.findById(id).orElseThrow();
//
//        Assertions.assertEquals("Jorge", newProfesor.getNombre());
//    }
//
//    @Test
//    public void testUnauthorizedSaveProfesor() throws Exception{
//        NewProfesorDto newProfesorDto = new NewProfesorDto();
//        newProfesorDto.setApellido("Rios");
//        newProfesorDto.setNombre("Jorge");
//        newProfesorDto.setEmail("jorgerios@utec.edu.pe");
//        newProfesorDto.setPassword("DBPfiltro");
//
//
//        mockMvc.perform(MockMvcRequestBuilders.post("/profesor")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(newProfesorDto)))
//                .andExpect(status().isForbidden());
//
//    }
//
//    @Test
//    @WithMockUser(value = "renato.garcia@utec.edu.pe", roles = "PROFESOR")
//    public void testGetProfesor() throws Exception {
//        profesorRepository.save(profesor);
//
//        mockMvc.perform(MockMvcRequestBuilders.get("/profesor/{id}", profesor.getId()))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("renato.garcia@utec.edu.pe"))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.nombre").value("Renato"))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.salones.[0].nombre").value("Salon1"))
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    public void testUnauthorizedGetProfesor() throws Exception {
//        profesorRepository.save(profesor);
//
//        mockMvc.perform(MockMvcRequestBuilders.get("/profesor/{id}", profesor.getId()))
//                .andExpect(status().isForbidden());
//    }
//
//    @Test
//    @WithMockUser(value = "renato.garcia@utec.edu.pe", roles = "PROFESOR")
//    public void testGetMeProfesor() throws Exception {
//
//        profesorRepository.save(profesor);
//
//        mockMvc.perform(MockMvcRequestBuilders.get("/profesor/me").contentType(MediaType.APPLICATION_JSON))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("renato.garcia@utec.edu.pe"))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.nombre").value("Renato"))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.salones.[0].nombre").value("Salon1"))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.publicaciones.[0].descripcion").value("Esta es una descripción"))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.comentarios[0].contenido").value("Este es un comentario"))
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    public void testUnauthorizedGetMeProfesor() throws Exception {
//
//        profesorRepository.save(profesor);
//
//        mockMvc.perform(MockMvcRequestBuilders.get("/profesor/me").contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isForbidden());
//    }
//
//    @Test
//    @WithMockUser(roles = {"ADMIN"})
//    public void testDeleteProfesor() throws Exception{
//        Profesor profesor1 = profesorRepository.save(profesor);
//
//        mockMvc.perform(MockMvcRequestBuilders.delete("/profesor/{id}", profesor1.getId()))
//                .andExpect(status().isNoContent());
//    }
//
//    @Test
//    public void testUnauthorizedDeleteProfesor() throws Exception{
//        Profesor profesor1 = profesorRepository.save(profesor);
//
//        mockMvc.perform(MockMvcRequestBuilders.delete("/profesor/{id}", profesor1.getId()))
//                .andExpect(status().isForbidden());
//    }
//
//
//    @Test
//    @WithMockUser(roles = {"ADMIN", "PROFESOR"})
//    public void testPatchProfesor() throws Exception{
//        NewProfesorDto newProfesorDto = new NewProfesorDto();
//        newProfesorDto.setApellido("Rios");
//        newProfesorDto.setNombre("Jorge");
//        newProfesorDto.setEmail("jorgerios@utec.edu.pe");
//        newProfesorDto.setPassword(passwordEncoder.encode("dBPFILTRo"));
//
//        Profesor profesor1 = profesorRepository.save(profesor);
//
//        mockMvc.perform(MockMvcRequestBuilders.patch("/profesor/{id}", profesor1.getId())
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(newProfesorDto)))
//                .andExpect(status().isOk());
//
//        Profesor profesor2 = profesorRepository.findById(profesor1.getId()).orElseThrow();
//        Assertions.assertEquals("Rios", profesor2.getApellido());
//        Assertions.assertEquals("jorgerios@utec.edu.pe", profesor2.getEmail());
//        Assertions.assertEquals("Jorge", profesor2.getNombre());
//
//    }
//
//    @Test
//    public void testUnauthorizedPatchProfesor() throws Exception{
//        NewProfesorDto newProfesorDto = new NewProfesorDto();
//        newProfesorDto.setApellido("Rios");
//        newProfesorDto.setNombre("Jorge");
//        newProfesorDto.setEmail("jorgerios@utec.edu.pe");
//        newProfesorDto.setPassword(passwordEncoder.encode("dBPFILTRo"));
//
//        Profesor profesor1 = profesorRepository.save(profesor);
//
//        mockMvc.perform(MockMvcRequestBuilders.patch("/profesor/{id}", profesor1.getId())
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(newProfesorDto)))
//                .andExpect(status().isForbidden());
//    }
//
//    @Test
//    @WithMockUser(roles = {"ADMIN", "PROFESOR", "PADRE"})
//    public void testGetPublicaciones() throws Exception{
//        profesorRepository.save(profesor);
//        mockMvc.perform(MockMvcRequestBuilders.get("/profesor/{id}/publicaciones", profesor.getId())
//                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].descripcion").value("Esta es una descripción"))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].titulo").value("Este es un título"))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].hijos[0].nombre").value("Eduardo"))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].hijos[1].nombre").value("Mikel"))
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    public void testUnauthorizedGetPublicaciones() throws Exception{
//        profesorRepository.save(profesor);
//        mockMvc.perform(MockMvcRequestBuilders.get("/profesor/{id}/publicaciones", profesor.getId())
//                        .contentType(MediaType.APPLICATION_JSON))
//               .andExpect(status().isForbidden());
//    }
//
//    @Test
//    @WithMockUser(value = "renato.garcia@utec.edu.pe", roles = "PROFESOR")
//    void testNewPassword() throws Exception {
//        profesorRepository.save(profesor);
//        NewPasswordDto newPasswordDto = new NewPasswordDto();
//        newPasswordDto.setPassword("2345678");
//        newPasswordDto.setEmail("renato.garcia@utec.edu.pe");
//
//        mockMvc.perform(MockMvcRequestBuilders.patch("/profesor/password")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(newPasswordDto)))
//                .andExpect(status().isOk());
//
//        Profesor profesor11 = profesorRepository.findByEmail(newPasswordDto.getEmail()).orElseThrow();
//        Assertions.assertTrue(passwordEncoder.matches("2345678", profesor11.getPassword()));
//    }
//
//    @Test
//    void testUnauthorizedNewPassword() throws Exception {
//        profesorRepository.save(profesor);
//        NewPasswordDto newPasswordDto = new NewPasswordDto();
//        newPasswordDto.setPassword("2345678");
//        newPasswordDto.setEmail("renato.garcia@utec.edu.pe");
//
//        mockMvc.perform(MockMvcRequestBuilders.patch("/profesor/password")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(newPasswordDto)))
//                .andExpect(status().isForbidden());
//    }
//}