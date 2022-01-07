package ch.heigvd.amt.ochap.usermgmt;

import java.time.Duration;
import java.util.Arrays;
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
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.View;

import ch.heigvd.amt.ochap.usermgmt.data.AccountInfoDTO;//TODO NGY Unused import statement
import ch.heigvd.amt.ochap.usermgmt.data.LoginDTO;
import ch.heigvd.amt.ochap.usermgmt.data.RegisterDTO;
import ch.heigvd.amt.ochap.usermgmt.data.TokenDTO;//TODO NGY Unused import statement
import ch.heigvd.amt.ochap.usermgmt.service.AmtAuthService;
import ch.heigvd.amt.ochap.usermgmt.service.AmtAuthService.IncorrectCredentialsException;
import ch.heigvd.amt.ochap.usermgmt.service.AmtAuthService.PropertyError;
import ch.heigvd.amt.ochap.usermgmt.service.AmtAuthService.UnacceptableRegistrationException;
import ch.heigvd.amt.ochap.usermgmt.service.AmtAuthService.UsernameAlreadyExistsException;

@Controller
public class Routes {
  @Autowired
  AmtAuthService authServer;

  // Send strayed users back to the homepage.
  @GetMapping(value = "/")
  public void method(@RequestParam Map<String, String> queryParams,
      HttpServletResponse httpServletResponse) {
    String backUrl = queryParams.getOrDefault("back", "/");
    httpServletResponse.setHeader("Location", backUrl);
    httpServletResponse.setStatus(302);
  }

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

  private Cookie tryLogin(String username, String password) {
    var dto = authServer.login(username, password).block(Duration.ofSeconds(10));
    Cookie authCookie = new Cookie("token", dto.getToken());
    authCookie.setHttpOnly(true);
    authCookie.setPath("/");
    return authCookie;
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
      var authCookie = tryLogin(loginDTO.getUsername(), loginDTO.getPassword());
      request.setAttribute(View.RESPONSE_STATUS_ATTRIBUTE, HttpStatus.SEE_OTHER);
      response.addCookie(authCookie);
      return "redirect:" + callbackUrl;
    } catch (RuntimeException re) {
      var e = re.getCause();
      if (IncorrectCredentialsException.class.isInstance(e)) {
        bindingResult.addError(new ObjectError("globalError",
            "We could not authenticate you with the provided username and password."));
        return "login";
      }
      model.addAttribute("explaination",
          "An unexpected error occured while processing your request.");
      model.addAttribute("backUrl", "/login");
      return "simple-error";
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
      authServer.register(username, password).block(Duration.ofSeconds(10));
    } catch (RuntimeException re) {
      var e = re.getCause();
      if (UsernameAlreadyExistsException.class.isInstance(e)) {
        bindingResult.addError(new FieldError("registerDTO", "username",
            "This username is not available. Please choose a different username."));
        return "register";
      } else if (UnacceptableRegistrationException.class.isInstance(e)) {
        var errors = ((UnacceptableRegistrationException) e).getErrors();
        if (errors.size() == 0) {
          model.addAttribute("explaination",
              "A server further in the processing chain returned an error.");
          model.addAttribute("backUrl", "/register");
          return "simple-error";
        }
        for (PropertyError error : errors) {
          String msg = error.getMessage();
          String prop = error.getProperty().toLowerCase();
          switch (prop) {
            case "username":
              bindingResult.addError(new FieldError("registerDTO", prop, msg));
              break;
            case "password":
              bindingResult.addError(new FieldError("registerDTO", prop, msg));
              break;
            default:
              bindingResult.addError(new ObjectError("globalError", prop + ": " + msg));
          }
        }
        return "register";
      }
      model.addAttribute("explaination",
          "An unexpected error occured while processing your request.");
      model.addAttribute("backUrl", "/register");
      return "simple-error";
    }

    try {
      var authCookie = tryLogin(username, password);
      request.setAttribute(View.RESPONSE_STATUS_ATTRIBUTE, HttpStatus.SEE_OTHER);
      response.addCookie(authCookie);
      return "redirect:" + callbackUrl;
    } catch (RuntimeException e) {
      model.addAttribute("explaination",
          "You account seems to have been succesfully created but we were "
              + "unable to automatically log you in. "
              + "Please try going back and loging in manually.");
      model.addAttribute("backUrl", "/login");
      return "simple-error";
    }
  }

  @GetMapping("/logout")
  public void doLogout(@RequestParam Map<String, String> queryParams, HttpServletRequest request,
      HttpServletResponse response) {
    String backUrl = queryParams.getOrDefault("back", "/");
    response.setHeader("Location", backUrl);
    response.setStatus(302);

    var cookies = request.getCookies();
    if (cookies == null)
      return;

    var tokenCookie = Arrays.stream(cookies).filter(c -> "token".equals(c.getName())).peek(c -> {
      c.setMaxAge(0);
      c.setValue(null);
      c.setPath("/");
    }).findAny();
    if (tokenCookie.isPresent())
      response.addCookie(tokenCookie.get());
  }
}
