package org.control_parental.hijo.controller;

import org.control_parental.hijo.domain.Hijo;
import org.control_parental.hijo.domain.HijoService;
import org.control_parental.hijo.dto.HijoResponseDto;
import org.control_parental.hijo.dto.NewHijoDto;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class HijoControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private HijoService hijoService;

    @Test
    public void testGetStudentById() throws Exception {
        HijoResponseDto hijoResponseDto = new HijoResponseDto();
        hijoResponseDto.setNombre("Test");
        hijoResponseDto.setApellido("Hijo");
        when(hijoService.getStudentById(any(Long.class))).thenReturn(hijoResponseDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/hijo/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Test"))
                .andExpect(jsonPath("$.apellido").value("Hijo"));
    }

    @Test
    public void testCreateStudent() throws Exception {
        NewHijoDto newHijoDto = new NewHijoDto();
        newHijoDto.setNombre("Test");
        newHijoDto.setApellido("Hijo");

        mockMvc.perform(MockMvcRequestBuilders.post("/hijo")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nombre\":\"Test\",\"apellido\":\"Hijo\"}"))
                .andExpect(status().isCreated());
    }

    @Test
    public void testDeleteStudent() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/hijo/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testGetPublicaciones() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/hijo/1/publicaciones")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testUpdateStudent() throws Exception { // patch
        NewHijoDto newHijoDto = new NewHijoDto();
        newHijoDto.setNombre("Test");
        newHijoDto.setApellido("Hijo");

        mockMvc.perform(MockMvcRequestBuilders.patch("/hijo/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nombre\":\"Test\",\"apellido\":\"Hijo\"}"))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetAllHijos() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/hijo")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    // este no funciona:
    @Test
    public void testSaveCSVStudents() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.multipart("/hijo/csv")
                        .file("file", "test.csv".getBytes())
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isCreated());
    }
}
