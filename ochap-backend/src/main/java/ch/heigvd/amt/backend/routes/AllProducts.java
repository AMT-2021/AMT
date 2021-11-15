package ch.heigvd.amt.backend.routes;

import ch.heigvd.amt.backend.entities.Product;
import ch.heigvd.amt.backend.repository.ProductDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;


@Controller
public class AllProducts {

  @Autowired
  private ProductDAO productDAO;

  @GetMapping("/all-products")
  public String allProduct(Model model, @RequestParam(required = false) String category) {
    Optional<List<Product>> hasProducts;
    Product[] products = new Product[]{};
    if (category != null) {
      // products =  new String[]{"product 1", "product 2", "product 3", "product 4"};
    } else {
      hasProducts = productDAO.getAllProducts();
      if (hasProducts.isPresent()) {
        products = hasProducts.get().toArray(new Product[0]);
      }
    }

    String[] categories = new String[] {"cat 1", "cat 2", "cat 3", "cat 4", "cat 5", "cat 6"};
    model.addAttribute("title", "All Products");
    model.addAttribute("categories", categories);
    model.addAttribute("products", products);
    return "all-products";
  }
}
