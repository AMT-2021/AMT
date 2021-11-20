package ch.heigvd.amt.backend.routes;

import ch.heigvd.amt.backend.entities.Category;
import ch.heigvd.amt.backend.entities.Product;
import ch.heigvd.amt.backend.entities.ProductQuantity;
import ch.heigvd.amt.backend.entities.ShoppingCart;
import ch.heigvd.amt.backend.repository.CategoryDAO;
import ch.heigvd.amt.backend.repository.ProductDAO;
import ch.heigvd.amt.backend.repository.ProductQuantityDAO;
import ch.heigvd.amt.backend.repository.ShoppingCartDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Controller
public class ShoppingCartTemplate {

  @Autowired private ProductDAO productDAO;
  @Autowired private ShoppingCartDAO shoppingCartDAO;
  @Autowired private ProductQuantityDAO productQuantityDAO;

  @GetMapping("/shopping-cart")
  public String basicTemplate(Model model) {

    List<ProductQuantity> shoppingCart = getProductQuantitesByClientId(1).getBody();

    ProductQuantity[] items;
    if(shoppingCart != null){
      items = shoppingCart.toArray(ProductQuantity[]::new);
    } else{
      items = new ProductQuantity[]{};
    }

    model.addAttribute("products", items);
    model.addAttribute("postAddress", "/shopping-cart");
    return "shopping-cart-template";
  }

  private ResponseEntity<Product> getProductById(Integer productId) {
    Optional<Product> product = productDAO.getProductById(productId);
    return product
        .map(value -> ResponseEntity.ok().body(value))
        .orElseGet(() -> ResponseEntity.notFound().build());
  }

  private ResponseEntity<Product> getProductQuantities(Integer productId) {
    Optional<Product> product = productDAO.getProductById(productId);
    return product
        .map(value -> ResponseEntity.ok().body(value))
        .orElseGet(() -> ResponseEntity.notFound().build());
  }

  private ResponseEntity<List<ProductQuantity>> getProductQuantitesByClientId(Integer shoppingCartId) {
    Optional<ShoppingCart> cart = shoppingCartDAO.findByClientId(shoppingCartId);
    if(cart.isPresent()){
      Optional<List<ProductQuantity>> products = productQuantityDAO.findByShoppingCartId(cart.get().getId());
      return products.map(value -> ResponseEntity.ok().body(value))
              .orElseGet(() -> ResponseEntity.notFound().build());
    }else{
      System.out.println("On est mal.");
      return ResponseEntity.notFound().build();
    }
  }

  @PostMapping(path = "/shopping-cart", consumes = MediaType.APPLICATION_JSON_VALUE)
  public void createEmployee(@RequestBody ProductDAO shoppingCart) {}
}
