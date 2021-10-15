package ch.heigvd.amt.backend.routes;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class BasicTemplate {
  @GetMapping("/basic-template")
  public String basicTemplate(Model model) {
    model.addAttribute("title", "Basic template");
    return "basic-template";
  }
}
