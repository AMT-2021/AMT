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
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
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
import ch.heigvd.amt.ochap.usermgmt.service.AmtAuthService.PropertyError;
import ch.heigvd.amt.ochap.usermgmt.service.AmtAuthService.UnacceptableRegistrationException;
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

  private String tryLoginAndMakeAuth(String username, String password) {
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
      String auth = tryLoginAndMakeAuth(username, password);
      request.setAttribute(View.RESPONSE_STATUS_ATTRIBUTE, HttpStatus.SEE_OTHER);
      response.addCookie(new Cookie("Authorization", auth));
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
}
