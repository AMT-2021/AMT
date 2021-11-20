package ch.heigvd.amt.backend.routes;

import ch.heigvd.amt.backend.entities.Product;
import ch.heigvd.amt.backend.repository.ProductDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Optional;

@Controller
public class ShoppingCartTemplate {

  @Autowired private ProductDAO productDAO;
  //@Autowired private ShoppingCartDAO shoppingCartDAO;

  @GetMapping("/shopping-cart")
  public String basicTemplate(Model model) {

    Product[] products = new Product[] {getProductById(2).getBody()};

    model.addAttribute("products", products);
    model.addAttribute("postAddress", "/shopping-cart");
    return "shopping-cart-template";
  }

  private ResponseEntity<Product> getProductById(Integer productId) {
    Optional<Product> product = productDAO.getProductById(productId);
    return product
        .map(value -> ResponseEntity.ok().body(value))
        .orElseGet(() -> ResponseEntity.notFound().build());
  }

  @PostMapping(path = "/shopping-cart", consumes = MediaType.APPLICATION_JSON_VALUE)
  public void createEmployee(@RequestBody ProductDAO shoppingCart) {

  }
}
