package ch.heigvd.amt.backend.routes;

import ch.heigvd.amt.backend.entities.Category;
import ch.heigvd.amt.backend.entities.Product;
import ch.heigvd.amt.backend.repository.CategoryDAO;
import ch.heigvd.amt.backend.repository.ProductDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;


@Controller
public class AllProducts {

  @Autowired
  private ProductDAO productDAO;

  @Autowired
  private CategoryDAO categoryDAO;

  @GetMapping("/all-products")
  public String allProduct(Model model, @RequestParam(required = false) String category) {
    Optional<List<Product>> hasProducts;
    List<Product> products = new ArrayList<>();
    if (category != null) {
      Optional<Category> cat = categoryDAO.getCategoryById(Integer.parseInt(category));
      if (cat.isPresent()) {
        Optional<List<Product>> testProduct = productDAO.getProductsByCategoryId(cat.get().getId());
        if (testProduct.isPresent()) {
          products = testProduct.get();
        }
      }
    } else {
      hasProducts = productDAO.getAllProducts();
      if (hasProducts.isPresent()) {
        products = hasProducts.get();
      }
    }

    Optional<List<Category>> cats = categoryDAO.getAllCategory();
    cats.ifPresent(categories -> model.addAttribute("categories", categories));
    model.addAttribute("title", "All Products");
    model.addAttribute("products", products);
    return "all-products";
  }
}
