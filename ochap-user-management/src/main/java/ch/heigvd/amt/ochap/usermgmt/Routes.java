package ch.heigvd.amt.ochap.usermgmt;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class Routes {
  @GetMapping("/test-oups-page")
  public String oups(Model model) {
    model.addAttribute("problemTitle", "Test oups page.");
    model.addAttribute("explaination", "We're working on it.");
    model.addAttribute("backUrl", "/");
    return "simple-error";
  }
}
