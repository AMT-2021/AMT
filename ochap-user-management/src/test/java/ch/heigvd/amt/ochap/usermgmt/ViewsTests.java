package ch.heigvd.amt.ochap.usermgmt;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import ch.heigvd.amt.ochap.usermgmt.data.AccountInfoDTO;
import ch.heigvd.amt.ochap.usermgmt.data.TokenDTO;
import ch.heigvd.amt.ochap.usermgmt.service.AmtAuthService;
import ch.heigvd.amt.ochap.usermgmt.service.AmtAuthService.IncorrectCredentialsException;
import ch.heigvd.amt.ochap.usermgmt.service.AmtAuthService.PropertyError;
import ch.heigvd.amt.ochap.usermgmt.service.AmtAuthService.UnacceptableRegistrationException;
import ch.heigvd.amt.ochap.usermgmt.service.AmtAuthService.UsernameAlreadyExistsException;
import reactor.core.publisher.Mono;

@SpringBootTest
@AutoConfigureMockMvc
public class ViewsTests {
  @Autowired
  private MockMvc mvc;

  @MockBean
  private AmtAuthService authServer;

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
    String username = "test-username";
    String password = "test-password";
    TokenDTO token = new TokenDTO("some access token", new AccountInfoDTO());
    Mockito.when(authServer.login(username, password)).thenReturn(Mono.just(token));

    this.mvc
        .perform(post("/login?callback=/foo").contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .param("username", username).param("password", password))
        .andExpect(view().name("redirect:/foo"))
        .andExpect(cookie().value("token", token.getToken()));
  }

  @Test
  public void registerActionRedirectsAndSetsCookie() throws Exception {
    String username = "test-username";
    String password = "test-password";
    AccountInfoDTO info = new AccountInfoDTO();
    TokenDTO token = new TokenDTO("some access token", info);
    Mockito.when(authServer.register(username, password)).thenReturn(Mono.just(info));
    Mockito.when(authServer.login(username, password)).thenReturn(Mono.just(token));

    this.mvc
        .perform(post("/register?callback=/foo").contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .param("username", "test-username").param("password", "test-password"))
        .andExpect(view().name("redirect:/foo"))
        .andExpect(cookie().value("token", token.getToken()));
  }

  @Test
  public void loginActionValidationFailureContainsErrorMessages() throws Exception {
    this.mvc
        .perform(post("/login?callback=/foo").contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .param("username", "").param("password", ""))
        .andExpect(content().string(containsString("Username cannot be empty")))
        .andExpect(content().string(containsString("Password cannot be empty")));
  }

  @Test
  public void registerActionValidationFailureContainsErrorMessages() throws Exception {
    this.mvc
        .perform(post("/register?callback=/foo").contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .param("username", "u").param("password", "p"))
        .andExpect(content().string(containsString("Username must have at least 4 characters")))
        .andExpect(content().string(containsString("Password must have at least 4 characters")));
  }

  @Test
  public void registerYieldingUnprocessableDisplaysErrors() throws Exception {
    String username = "test-username";
    String password = "test-password";
    Mockito.when(authServer.register(username, password))
        .thenReturn(Mono.error(new UnacceptableRegistrationException(
            List.of(new PropertyError("username", "error in username"),
                new PropertyError("password", "error in password"),
                new PropertyError("some property", "other error")))));
    this.mvc
        .perform(post("/register?callback=/foo").contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .param("username", username).param("password", password))
        .andExpect(view().name("register"))
        .andExpect(content().string(containsString("error in username")))
        .andExpect(content().string(containsString("error in password")))
        .andExpect(content().string(containsString("some property: other error")));
  }

  @Test
  public void registerExistingUserYieldsErrorMessage() throws Exception {
    String username = "test-username";
    String password = "test-password";
    Mockito.when(authServer.register(username, password))
        .thenReturn(Mono.error(new UsernameAlreadyExistsException()));
    this.mvc
        .perform(post("/register?callback=/foo").contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .param("username", username).param("password", password))
        .andExpect(view().name("register"))
        .andExpect(content().string(containsString("This username is not available.")))
        .andExpect(content().string(containsString("Please choose a different username.")));
  }

  @Test
  public void registerThenFailedLoginDisplaysOops() throws Exception {
    String username = "test-username";
    String password = "test-password";
    AccountInfoDTO info = new AccountInfoDTO();
    Mockito.when(authServer.register(username, password)).thenReturn(Mono.just(info));
    Mockito.when(authServer.login(username, password))
        .thenReturn(Mono.error(new RuntimeException()));
    this.mvc
        .perform(post("/register?callback=/foo").contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .param("username", username).param("password", password))
        .andExpect(
            content().string(containsString("account seems to have been succesfully created")))
        .andExpect(content().string(containsString("unable to automatically log you in")))
        .andExpect(view().name("simple-error"));
  }

  @Test
  public void loginIncorrectCredentialsYieldsErrorMessage() throws Exception {
    String username = "test-username";
    String password = "test-password";
    Mockito.when(authServer.login(username, password))
        .thenReturn(Mono.error(new IncorrectCredentialsException()));

    this.mvc
        .perform(post("/login?callback=/foo").contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .param("username", username).param("password", password))
        .andExpect(content().string(containsString("could not authenticate you")))
        .andExpect(view().name("login"));
  }

  @Test
  public void loginUpstreamErrorDisplayOops() throws Exception {
    String username = "test-username";
    String password = "test-password";
    Mockito.when(authServer.login(username, password))
        .thenReturn(Mono.error(new RuntimeException()));

    this.mvc
        .perform(post("/login?callback=/foo").contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .param("username", username).param("password", password))
        .andExpect(content().string(containsString("error occured")))
        .andExpect(view().name("simple-error"));
  }
}
