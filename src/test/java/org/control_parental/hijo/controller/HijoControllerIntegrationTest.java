package org.control_parental.hijo.controller;

import org.control_parental.hijo.domain.Hijo;
import org.control_parental.hijo.domain.HijoService;
import org.control_parental.hijo.dto.HijoResponseDto;
import org.control_parental.hijo.dto.NewHijoDto;
import org.junit.jupiter.api.BeforeEach;
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

    }

    @Test
    public void testCreateStudent() throws Exception {

    }

    @Test
    public void testDeleteStudent() throws Exception {

    }

    @Test
    public void testGetPublicaciones() throws Exception {

    }

    @Test
    public void testUpdateStudent() throws Exception {

    }

    @Test
    public void testGetAllHijos() throws Exception {

    }

}
