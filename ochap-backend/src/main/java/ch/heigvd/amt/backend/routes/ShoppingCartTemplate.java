package ch.heigvd.amt.backend.routes;

import ch.heigvd.amt.backend.entities.ProductQuantity;
import ch.heigvd.amt.backend.entities.ShoppingCart;
import ch.heigvd.amt.backend.repository.ProductQuantityDAO;
import ch.heigvd.amt.backend.repository.ShoppingCartDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.persistence.EntityManager;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
public class ShoppingCartTemplate {

    @Autowired
    private ShoppingCartDAO shoppingCartDAO;
    @Autowired
    private ProductQuantityDAO productQuantityDAO;
    @Autowired
    private EntityManager em;

    @GetMapping("/shopping-cart")
    public String basicTemplate(Model model) {
        int clientId = 2;
        List<ProductQuantity> shoppingCart = getProductQuantitesByClientId(clientId).getBody();
        ShoppingCart cart = getShoppingCartByClientId(clientId).getBody();

        ProductQuantity[] items;
        if (shoppingCart != null) {
            items = shoppingCart.toArray(ProductQuantity[]::new);
        } else {
            items = new ProductQuantity[] {};
        }

        model.addAttribute("products", items);
        model.addAttribute("postAddress", "/shopping-cart");
        model.addAttribute("cart", cart);
        return "shopping-cart-template";
    }

    private ResponseEntity<List<ProductQuantity>> getProductQuantitesByClientId(Integer clientId) {
        Optional<ShoppingCart> cart = shoppingCartDAO.findByClientId(clientId);
        if (cart.isPresent()) {
            return getProductQuantitiesByShoppingCartId(cart.get().getId());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    private ResponseEntity<ShoppingCart> getShoppingCartByClientId(Integer clientId) {
        Optional<ShoppingCart> cart = shoppingCartDAO.findByClientId(clientId);
        return cart.map(shoppingCart -> ResponseEntity.ok().body(shoppingCart))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    private ResponseEntity<List<ProductQuantity>> getProductQuantitiesByShoppingCartId(
            Integer shoppingCartId) {
        Optional<List<ProductQuantity>> products =
                productQuantityDAO.findByShoppingCartId(shoppingCartId);
        return products.map(value -> ResponseEntity.ok().body(value))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping(path = "/shopping-cart/update")
    public String updateProduct(@Valid ProductQuantity updatedProduct) {
        System.out.println(updatedProduct);
        Optional<ProductQuantity> pQ =
                productQuantityDAO.getProductQuantityById(updatedProduct.getId());
        if (pQ.isPresent()) {
            ProductQuantity item = pQ.get();
            if (item.getProduct().getStock() > updatedProduct.getQuantity()) {
                if (updatedProduct.getQuantity() <= 0) {
                    removeOneProduct(item);
                } else {
                    item.setQuantity(updatedProduct.getQuantity());
                    productQuantityDAO.save(item);
                    em.detach(item); // Discard this entity's cache.
                }
            }
        }
        System.out.println("aaarrrggghhhh (1)");
        return "redirect:/shopping-cart";
    }

    @PostMapping(path = "/shopping-cart/remove")
    public String removeProduct(@Valid ProductQuantity productToRemove) {
        System.out.println(productToRemove);
        removeOneProduct(productToRemove);
        return "redirect:/shopping-cart";
    }

    @PostMapping(path = "/shopping-cart/clear-cart")
    public String clearCart(@Valid ShoppingCart toClear) {
        List<ProductQuantity> shoppingCart =
                getProductQuantitiesByShoppingCartId(toClear.getId()).getBody();
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
