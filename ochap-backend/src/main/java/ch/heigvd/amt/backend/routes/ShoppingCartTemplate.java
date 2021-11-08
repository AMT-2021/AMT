package ch.heigvd.amt.backend.routes;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ShoppingCartTemplate {
    @GetMapping("/shopping-cart")
    public String basicTemplate(Model model) {
        //model.addAttribute("title", "Basic template");
        return "shopping-cart-template";
    }
}
