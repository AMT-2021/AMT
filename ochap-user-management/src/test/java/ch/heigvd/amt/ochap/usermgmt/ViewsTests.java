package ch.heigvd.amt.ochap.usermgmt;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class ViewsTests {
  @Autowired
  private MockMvc mvc;

  @Test
  public void testOupsPage() throws Exception {
    mvc.perform(get("/test-oups-page")).andExpect(status().isOk())
        .andExpect(content().string(containsString("Test oups page.")))
        .andExpect(content().string(containsString("working on it.")))
        .andExpect(content().string(containsString("form action=\"/\"")))
        .andExpect(content().string(containsString("type=\"submit\" value=\"Go back\"")));
  }

  @Test
  public void getLoginReturnsLoginView() throws Exception {
    this.mvc.perform(get("/login?callback=/foo")).andExpect(view().name("login"));
  }

  @Test
  public void loginActionRedirectsAndSetsCookie() throws Exception {
    this.mvc
        .perform(post("/login?callback=/foo").contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .param("username", "test-username").param("password", "test-password"))
        .andExpect(view().name("redirect:/foo"))
        .andExpect(cookie().value("Authorization", "invalid-login-not-implemented"));
  }

  @Test
  public void loginActionValidationFailureContainsErrorMessages() throws Exception {
    this.mvc
        .perform(post("/login?callback=/foo").contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .param("username", "").param("password", ""))
        .andExpect(content().string(containsString("Username cannot be empty")))
        .andExpect(content().string(containsString("Password cannot be empty")));
  }
}
