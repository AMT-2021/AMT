package ch.heigvd.amt.backend.routes;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ShoppingCartTemplate {
  @GetMapping("/shopping-cart")
  public String basicTemplate(Model model) {
    String[] items = new String[] { "product1", "product2", "product3", "product4", "product5" };
    model.addAttribute("items", items);
    return "shopping-cart-template";
  }
}
