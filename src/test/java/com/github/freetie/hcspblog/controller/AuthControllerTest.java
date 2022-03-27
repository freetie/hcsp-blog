package com.github.freetie.hcspblog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.freetie.hcspblog.entity.User;
import com.github.freetie.hcspblog.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import javax.servlet.http.HttpSession;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserService userService;
    BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    @Test
    public void testLogin() throws Exception {
        Map<String, String> usernameAndPassword = new HashMap<>();
        usernameAndPassword.put("username", "username");
        usernameAndPassword.put("password", "password");
        when(userService.loadUserByUsername("username"))
                .thenReturn(new org.springframework.security.core.userdetails.User("username", bCryptPasswordEncoder.encode("password"), Collections.emptyList()));
        when(userService.getUserByUsername("username"))
                .thenReturn(new User(1, "username", bCryptPasswordEncoder.encode("password"), null, null, null));

        MvcResult loginResult = mockMvc.perform(
                        post("/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(new ObjectMapper().writeValueAsString(usernameAndPassword))
                                .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk())
                .andExpect(result -> assertThat(result.getResponse().getContentAsString(StandardCharsets.UTF_8).contains("登陆成功")).isTrue())
                .andReturn();

        HttpSession session = loginResult.getRequest().getSession();
        assertThat(session).isNotNull();
        mockMvc.perform(get("/auth").session((MockHttpSession) session))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("\"isLogin\":true")));
    }
}