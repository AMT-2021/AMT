package ch.heigvd.amt.backend.routes;

import ch.heigvd.amt.backend.repository.ProductDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class Product {
  @Autowired private ProductDAO productDAO;

  @GetMapping("/products/{id}")
  public String basicTemplate(Model model, @PathVariable int id) {
    var productOptional = productDAO.getProductById(id);
    if (productOptional.isEmpty()) {
      return "productNotFound";
    }

    var product = productOptional.get();
    model.addAttribute("id", product.getId());
    model.addAttribute("name", product.getName());
    model.addAttribute("price", product.getPrice());
    model.addAttribute("description", product.getDescription());
    model.addAttribute("image", "/icon/p1.png");
    return "product";
  }
}
