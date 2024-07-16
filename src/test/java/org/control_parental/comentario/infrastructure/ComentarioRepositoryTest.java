//package org.control_parental.comentario.infrastructure;
//
//import org.control_parental.AbstractContainerBaseTest;
//import org.control_parental.configuration.TestConfig;
//import org.control_parental.comentario.domain.Comentario;
//import org.control_parental.publicacion.domain.Publicacion;
//import org.control_parental.usuario.domain.Usuario;
//import org.control_parental.usuario.domain.Role;
//import org.control_parental.usuario.infrastructure.UsuarioRepository;
//import org.control_parental.publicacion.infrastructure.PublicacionRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
//import org.springframework.context.annotation.Import;
//
//import java.time.LocalDateTime;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@DataJpaTest
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//@Import(TestConfig.class)
//public class ComentarioRepositoryTest extends AbstractContainerBaseTest {
//
//    @Autowired
//    ComentarioRepository comentarioRepository;
//
//    @Autowired
//    UsuarioRepository usuarioRepository;
//
//    @Autowired
////    PublicacionRepository publicacionRepository;
//
//    @Autowired
//    TestEntityManager entityManager;
//
//    Usuario usuario;
//    Publicacion publicacion;
//    Comentario comentario1;
//    Comentario comentario2;
//
//    @BeforeEach
//    public void setUp() {
//        usuario = new Usuario();
//        usuario.setNombre("Laura");
//        usuario.setApellido("Nagamine");
//        usuario.setEmail("laura.nagamine@utec.edu.pe");
//        usuario.setPassword("password123");
//        entityManager.persist(usuario);
//
//        publicacion = new Publicacion();
//        publicacion.setTitulo("Publicacion 1");
//        publicacion.setDescripcion("Descripcion de la publicacion 1");
//        publicacion.setFecha(LocalDateTime.now());
//        publicacion.setLikes(0);
//
//        entityManager.persist(publicacion);
//
//        comentario1 = new Comentario();
//        comentario1.setFecha(LocalDateTime.now());
//        comentario1.setContenido("Comentario 1");
//        comentario1.setUsuario(usuario);
//        comentario1.setPublicacion(publicacion);
//        entityManager.persist(comentario1);
//
//        comentario2 = new Comentario();
//        comentario2.setFecha(LocalDateTime.now());
//        comentario2.setContenido("Comentario 2");
//        comentario2.setUsuario(usuario);
//        comentario2.setPublicacion(publicacion);
//        entityManager.persist(comentario2);
//
//        entityManager.flush();
//    }
//
//    @Test
//    public void createComentario() {
//        Comentario comentario = new Comentario();
//        comentario.setFecha(LocalDateTime.now());
//        comentario.setContenido("Nuevo Comentario");
//        comentario.setUsuario(usuario);
//        comentario.setPublicacion(publicacion);
//
//        Comentario savedComentario = comentarioRepository.save(comentario);
//        Optional<Comentario> retrievedComentario = comentarioRepository.findById(savedComentario.getId());
//        assertTrue(retrievedComentario.isPresent());
//        assertEquals("Nuevo Comentario", retrievedComentario.get().getContenido());
//    }
//
//    @Test
//    public void testFindById() {
//        Optional<Comentario> optionalComentario = comentarioRepository.findById(comentario1.getId());
//        assertTrue(optionalComentario.isPresent());
//        assertEquals("Comentario 1", optionalComentario.get().getContenido());
//    }
//
//    @Test
//    public void shouldDeleteComentario() {
//        comentarioRepository.deleteById(comentario1.getId());
//        assertTrue(comentarioRepository.findById(comentario1.getId()).isEmpty());
//    }
//
//    @Test
//    public void updateComentario() {
//        comentario1.setContenido("Comentario Actualizado");
//        comentarioRepository.save(comentario1);
//
//        Comentario retrievedComentario = comentarioRepository.findById(comentario1.getId()).orElseThrow();
//        assertEquals("Comentario Actualizado", retrievedComentario.getContenido());
//    }
//}
