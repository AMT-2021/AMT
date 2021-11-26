package ch.heigvd.amt.backend.routes;

import ch.heigvd.amt.backend.entities.ProductQuantity;
import ch.heigvd.amt.backend.entities.ShoppingCart;
import ch.heigvd.amt.backend.repository.ProductQuantityDAO;
import ch.heigvd.amt.backend.repository.ShoppingCartDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.persistence.EntityManager;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Controller
public class ShoppingCartPage {

  @Autowired
  private ShoppingCartDAO shoppingCartDAO;
  @Autowired
  private ProductQuantityDAO productQuantityDAO;

  @GetMapping("/shopping-cart")
  public String viewShoppingCart(Model model) {
    int clientId = 2;
    ShoppingCart cart = getShoppingCartByClientId(clientId);
    Set<ProductQuantity> products = cart.getProducts();

    model.addAttribute("products", products);
    model.addAttribute("cart", cart);
    return "shopping-cart";
  }

  private ShoppingCart getShoppingCartByClientId(Integer clientId) {
    Optional<ShoppingCart> cart = shoppingCartDAO.findByClientId(clientId);
    return cart.orElse(new ShoppingCart());
  }

  @PostMapping(path = "/shopping-cart/update")
  public String updateProduct(@Valid ProductQuantity updatedProduct) {
    Optional<ProductQuantity> pQ =
        productQuantityDAO.getProductQuantityById(updatedProduct.getId());
    if (pQ.isEmpty()) {
      return "redirect:/shopping-cart";
    }
    ProductQuantity item = pQ.get();
    if (item.getProduct().getStock() > updatedProduct.getQuantity()) {
      if (updatedProduct.getQuantity() <= 0) {
        removeOneProduct(item);
      } else {
        item.setQuantity(updatedProduct.getQuantity());
        productQuantityDAO.save(item);
      }
    }
    return "redirect:/shopping-cart";
  }

  @PostMapping(path = "/shopping-cart/remove")
  public String removeProduct(@Valid ProductQuantity productToRemove) {
    removeOneProduct(productToRemove);
    return "redirect:/shopping-cart";
  }

  @PostMapping(path = "/shopping-cart/clear-cart")
  public String clearCart(@Valid ShoppingCart toClear) {
    List<ProductQuantity> shoppingCart = productQuantityDAO.findByShoppingCartId(toClear.getId());
    for (ProductQuantity item : shoppingCart) {
      removeOneProduct(item);
    }
    return "redirect:/shopping-cart";
  }

  private void removeOneProduct(@Valid ProductQuantity item) {
    Optional<ProductQuantity> pQ = productQuantityDAO.getProductQuantityById(item.getId());
    pQ.ifPresent(productQuantity -> productQuantityDAO.delete(productQuantity));
  }
}
