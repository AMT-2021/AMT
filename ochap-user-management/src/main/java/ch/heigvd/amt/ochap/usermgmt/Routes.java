package ch.heigvd.amt.ochap.usermgmt;

import java.time.Duration;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.View;

import ch.heigvd.amt.ochap.usermgmt.data.AccountInfoDTO;
import ch.heigvd.amt.ochap.usermgmt.data.LoginDTO;
import ch.heigvd.amt.ochap.usermgmt.data.RegisterDTO;
import ch.heigvd.amt.ochap.usermgmt.data.TokenDTO;
import ch.heigvd.amt.ochap.usermgmt.service.AmtAuthService;
import ch.heigvd.amt.ochap.usermgmt.service.AmtAuthService.IncorrectCredentialsException;
import ch.heigvd.amt.ochap.usermgmt.service.AmtAuthService.UsernameAlreadyExistsException;

@Controller
public class Routes {
  @Autowired
  AmtAuthService authServer;

  @GetMapping("/test-oups-page")
  public String oups(Model model) {
    model.addAttribute("problemTitle", "Test oups page.");
    model.addAttribute("explaination", "We're working on it.");
    model.addAttribute("backUrl", "/");
    return "simple-error";
  }

  @GetMapping("/login")
  public String loginView(Model model, LoginDTO loginDTO) {
    model.addAttribute("callbackUrl", "/");
    return "login";
  }

  private String tryLoginAndMakeAuth(String username, String password)
      throws IncorrectCredentialsException {
    TokenDTO upstreamLogin =
        authServer.login(username, password).timeout(Duration.ofSeconds(10)).block();
    return "Bearer " + upstreamLogin.getToken();
  }

  @PostMapping(path = "/login")
  public String loginPerform(@RequestParam Map<String, String> queryParams,
      @Valid LoginDTO loginDTO, BindingResult bindingResult, Model model,
      HttpServletRequest request, HttpServletResponse response) {
    String callbackUrl = queryParams.getOrDefault("callback", "/");
    model.addAttribute("callbackUrl", callbackUrl);
    if (bindingResult.hasErrors()) {
      return "login";
    }

    try {
      String auth = tryLoginAndMakeAuth(loginDTO.getUsername(), loginDTO.getPassword());
      request.setAttribute(View.RESPONSE_STATUS_ATTRIBUTE, HttpStatus.SEE_OTHER);
      response.addCookie(new Cookie("Authorization", auth));
      return "redirect:" + callbackUrl;
    } catch (IncorrectCredentialsException e) {
      // Let the user know the specified credentials were not valid.
      throw new RuntimeException("Not implemented.");
    }
  }

  @GetMapping("/register")
  public String registerView(Model model, RegisterDTO registerDTO) {
    model.addAttribute("callbackUrl", "/");
    return "register";
  }

  @PostMapping("/register")
  public String registerPerform(@RequestParam Map<String, String> queryParams,
      @Valid RegisterDTO registerDTO, BindingResult bindingResult, Model model,
      HttpServletRequest request, HttpServletResponse response) {
    String callbackUrl = queryParams.getOrDefault("callback", "/");
    model.addAttribute("callbackUrl", callbackUrl);
    if (bindingResult.hasErrors()) {
      return "register";
    }

    String username = registerDTO.getUsername();
    String password = registerDTO.getPassword();

    try {
      AccountInfoDTO registration =
          authServer.register(username, password).timeout(Duration.ofSeconds(10)).block();
    } catch (UsernameAlreadyExistsException e) {
      // Let the view know the chosen username is not available.
      // Perhaphs they would like to log in.
      throw new RuntimeException("Not implemented.");
    }

    try {
      String auth = tryLoginAndMakeAuth(username, password);
      request.setAttribute(View.RESPONSE_STATUS_ATTRIBUTE, HttpStatus.SEE_OTHER);
      response.addCookie(new Cookie("Authorization", auth));
      return "redirect:" + callbackUrl;
    } catch (IncorrectCredentialsException e) {
      // This should not happen, show a nice message...
      throw new RuntimeException("Not implemented.");
    }
  }
}
