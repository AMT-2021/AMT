package ch.heigvd.amt.ochap.usermgmt;

import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.View;

import ch.heigvd.amt.ochap.usermgmt.data.LoginDTO;

@Controller
public class Routes {
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

  @PostMapping(path = "/login")
  public String loginPerform(@RequestParam Map<String, String> queryParams,
      @Valid LoginDTO loginDTO, BindingResult bindingResult, Model model,
      HttpServletRequest request, HttpServletResponse response) {
    String callbackUrl = queryParams.getOrDefault("callback", "/");
    model.addAttribute("callbackUrl", callbackUrl);
    if (bindingResult.hasErrors()) {
      return "login";
    }

    request.setAttribute(View.RESPONSE_STATUS_ATTRIBUTE, HttpStatus.SEE_OTHER);
    response.addCookie(new Cookie("Authorization", "invalid-login-not-implemented"));
    return "redirect:" + callbackUrl;
  }

  @GetMapping("/register")
  public String registerView(Model model) {
    model.addAttribute("callbackUrl", "/");
    model.addAttribute("cancelUrl", "/");
    return "register";
  }
}
