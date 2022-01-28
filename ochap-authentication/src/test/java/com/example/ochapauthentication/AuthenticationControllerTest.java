package com.example.ochapauthentication;

import com.example.ochapauthentication.commands.AccountRegisterCommand;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.TestPropertySourceUtils;
import org.springframework.test.web.servlet.MockMvc;

import javax.transaction.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = OchapAuthenticationApplication.class)
@AutoConfigureMockMvc
@ContextConfiguration(initializers = AuthenticationControllerTest.ContextInitializer.class)
public class AuthenticationControllerTest {
  @Autowired
  private MockMvc mvc;

  private final ObjectMapper om = new ObjectMapper();

  public static class ContextInitializer
      implements ApplicationContextInitializer<ConfigurableApplicationContext> {
    @Override
    public void initialize(ConfigurableApplicationContext configurableApplicationContext) {

      TestPropertySourceUtils.addInlinedPropertiesToEnvironment(configurableApplicationContext,
          "jwt.secret=invalid", "admin.password=invalid");
    }
  }

  @Test
  @Transactional
  public void newUserCanRegister() throws Exception {
    AccountRegisterCommand credentials = new AccountRegisterCommand();
    credentials.setUsername("TestUser");
    credentials.setPassword("TestUser123");
    String requestJson = om.writeValueAsString(credentials);
    this.mvc
        .perform(
            post("/accounts/register").contentType(MediaType.APPLICATION_JSON).content(requestJson))
        .andExpect(status().isCreated());
  }

  @Test
  @Transactional
  public void newCredentialsIncorrect() throws Exception {
    AccountRegisterCommand credentials = new AccountRegisterCommand();
    credentials.setUsername("TestUser");
    credentials.setPassword("TestUser123");
    String requestJson = om.writeValueAsString(credentials);
    this.mvc
        .perform(
            post("/accounts/register").contentType(MediaType.APPLICATION_JSON).content(requestJson))
        .andExpect(status().isCreated());
  }

  @Test
  @Transactional
  public void cantHaveSameUsername() throws Exception {
    AccountRegisterCommand credentials = new AccountRegisterCommand();
    credentials.setUsername("TestUser");
    credentials.setPassword("TestUser123");
    String requestJson = om.writeValueAsString(credentials);

    this.mvc.perform(
        post("/accounts/register").contentType(MediaType.APPLICATION_JSON).content(requestJson));

    this.mvc
        .perform(
            post("/accounts/register").contentType(MediaType.APPLICATION_JSON).content(requestJson))
        .andExpect(status().isConflict());
  }


}
