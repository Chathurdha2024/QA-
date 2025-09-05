package com.example.backend.controller;

import com.example.backend.model.User;
import com.example.backend.repository.UserRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MainController.class)
class MainControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepo userRepo;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId("12345");
        testUser.setUsername("Hasangi");
        testUser.setEmail("hasangi@gmail.com");
        testUser.setPassword("hasangi123");
    }

    @Test
    void testSignup_NewUser_ShouldRegisterSuccessfully() throws Exception {
        Mockito.when(userRepo.findByUsername("Hasangi")).thenReturn(null);
        Mockito.when(userRepo.save(Mockito.any(User.class))).thenReturn(testUser);

        mockMvc.perform(post("/api/addUser")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"Hasangi\",\"email\":\"hasangi@gmail.com\",\"password\":\"hasangi123\"}"))
                .andExpect(status().isCreated()) // ✅ 201 Created
                .andExpect(jsonPath("$.message").value("User Registered Successfully")) // ✅ exact text
                .andExpect(jsonPath("$.id").value("12345"))
                .andExpect(jsonPath("$.username").value("Hasangi"))
                .andExpect(jsonPath("$.email").value("hasangi@gmail.com"));
    }

    @Test
    void testSignup_ExistingUser_ShouldFail() throws Exception {
        Mockito.when(userRepo.findByUsername("Hasangi")).thenReturn(testUser);

        mockMvc.perform(post("/api/addUser")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"Hasangi\",\"email\":\"hasangi@gmail.com\",\"password\":\"hasangi123\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Username already exists"));
    }

    @Test
    void testLogin_ValidCredentials_ShouldSucceed() throws Exception {
        Mockito.when(userRepo.findByUsernameAndPassword("Hasangi", "hasangi123")).thenReturn(testUser);

        mockMvc.perform(post("/api/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"Hasangi\",\"password\":\"hasangi123\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Login successful"))
                .andExpect(jsonPath("$.token").isNotEmpty()); // ✅ only token returned
    }

    @Test
    void testLogin_InvalidCredentials_ShouldFail() throws Exception {
        Mockito.when(userRepo.findByUsernameAndPassword("Hasangi", "wrongpass")).thenReturn(null);

        mockMvc.perform(post("/api/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"Hasangi\",\"password\":\"wrongpass\"}"))
                .andExpect(status().isBadRequest()) // ✅ 400, not 401
                .andExpect(jsonPath("$.message").value("Invalid username or password"));
    }
}
