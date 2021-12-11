package ch.heigvd.amt.backend.routes;

import ch.heigvd.amt.backend.entities.Product;
import ch.heigvd.amt.backend.entities.ProductQuantity;
import ch.heigvd.amt.backend.entities.ShoppingCart;
import ch.heigvd.amt.backend.repository.ProductDAO;
import ch.heigvd.amt.backend.repository.ProductQuantityDAO;
import ch.heigvd.amt.backend.repository.ShoppingCartDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.*;

@Controller
public class ShoppingCartPage {

  @Autowired
  private ShoppingCartDAO shoppingCartDAO;
  @Autowired
  private ProductQuantityDAO productQuantityDAO;
  @Autowired
  private ProductDAO productDAO;

  @GetMapping("/shopping-cart")
  public String viewShoppingCart(Model model) {
    int clientId = 2;
    ShoppingCart cart = getShoppingCartByClientId(clientId);
    List<ProductQuantity> products = cart.getProducts();

    model.addAttribute("products", products);
    model.addAttribute("cart", cart);
    return "shopping-cart";
  }

  private ShoppingCart getShoppingCartByClientId(Integer clientId) {
    Optional<ShoppingCart> cart = shoppingCartDAO.findByClientId(clientId);
    if (cart.isPresent()) {
      return cart.get();
    }

    ShoppingCart newCart = new ShoppingCart();
    newCart.setClientId(clientId);
    newCart.setProducts(new ArrayList<>());
    return newCart;
  }

  @PostMapping(path = "/shopping-cart/add",
          consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
  public String addProduct(@RequestParam Map<String, String> productQuantityToAdd) {
    int clientId = 2;
    int id = Integer.parseInt(productQuantityToAdd.get("id"));
    int quantity = Integer.parseInt(productQuantityToAdd.get("quantity"));

    ShoppingCart cart = getShoppingCartByClientId(clientId);
    ProductQuantity productQuantity = null;
    for (ProductQuantity p : cart.getProducts()) {
      if (p.getProduct().getId().equals(id)) {
        productQuantity = p;
        break;
      }
    }

    if (productQuantity == null) {
      productQuantity = new ProductQuantity();
      productQuantity.setQuantity(0);
      Optional<Product> product = productDAO.getProductById(id);
      if (product.isPresent()) {
        productQuantity.setProduct(product.get());
        cart.getProducts().add(productQuantity);
        productQuantity.setShoppingCart(cart);
      }
    }

    if (quantity > 0
            && productQuantity.getProduct().getStock() > quantity + productQuantity.getQuantity()) {
      productQuantity.setQuantity(productQuantity.getQuantity() + quantity);
      productQuantityDAO.save(productQuantity);
      shoppingCartDAO.save(cart);
    }
    return "redirect:/shopping-cart";
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
