package ch.heigvd.amt.backend.routes;

import ch.heigvd.amt.backend.repository.ProductDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class Product {
  @Autowired //TODO NGY Field injection is not recommended
  private ProductDAO productDAO;

  @GetMapping("/products/{id}")
  public String viewProduct(Model model, @PathVariable int id) {
    var product = productDAO.getProductById(id).orElse(null);

    model.addAttribute("product", product);
    model.addAttribute("quantity", 1);
    model.addAttribute("image", "/icon/p1.png");
    return "product";
  }
}
