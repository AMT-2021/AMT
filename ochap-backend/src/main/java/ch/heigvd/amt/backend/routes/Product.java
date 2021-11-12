package ch.heigvd.amt.backend.routes;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class Product {
  @GetMapping("/products/{id}")
  public String basicTemplate(Model model, @PathVariable int id) {
    model.addAttribute("id", id);
    model.addAttribute("name", "Product name");
    model.addAttribute("price", 130);
    model.addAttribute("description", "The description of this product will make you want to buy it immediately!");
    model.addAttribute("image", "https://via.placeholder.com/720x480?text=This+image+is+not+contractual");
    return "product";
  }
}
