package com.example.ochapauthentication;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import javax.transaction.Transactional;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = OchapAuthenticationApplication.class)
@AutoConfigureMockMvc
public class AuthenticationControllerTest {
    @Autowired
    private MockMvc mvc;

    @Test
    @Transactional
    public void newUserCanRegister() throws Exception {
        String username = "testUser";
        String password = "testUser123";
        this.mvc.perform(post("/accounts/register")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .param("username", username)
                .param("password", password))
            .andExpect(status().is2xxSuccessful());
    }
}
