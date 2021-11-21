package ch.heigvd.amt.backend;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class LandingPageViewTests {
  @Autowired
  private MockMvc mvc;

  @Test
  public void headerHasLoginSignupWhenNotAuthenticated() throws Exception {
    mvc.perform(get("/")).andExpect(status().isOk())
        .andExpect(content().string(containsString("Log in")))
        .andExpect(content().string(containsString("Signup")))
        .andExpect(content().string(not(containsString("Log out"))));
  }

  @Test
  @WithMockUser(username = "test-user")
  public void headerHasLogoutWhenAuthenticated() throws Exception {
    mvc.perform(get("/")).andExpect(status().isOk())
        .andExpect(content().string(not(containsString("Log in"))))
        .andExpect(content().string(not(containsString("Signup"))))
        .andExpect(content().string(containsString("Hello <span>test-user</span>!")))
        .andExpect(content().string(containsString("Log out")));
  }
}
