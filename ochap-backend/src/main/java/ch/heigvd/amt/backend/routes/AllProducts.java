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
import java.util.List;
import java.util.Optional;


@Controller
public class AllProducts {

  @Autowired
  private ProductDAO productDAO;

  @Autowired
  private CategoryDAO categoryDAO;

  @GetMapping("/all-products")
  public String allProduct(Model model, @RequestParam(required = false) String categoryId) {
    Optional<List<Product>> hasProducts;
    List<Product> products = new ArrayList<>();
    if (categoryId != null) {
      Category c = categoryDAO.getCategoryById(Integer.parseInt(categoryId)).get();
      products = c.getProducts();

    } else {
      hasProducts = productDAO.getAllProducts();
      if (hasProducts.isPresent()) {
        products = hasProducts.get();
      }
    }

    List<Category> categories = categoryDAO.getAllCategory().get();
    List<Category> catToRemove = new ArrayList<Category>();
    for (Category c : categories) {
      if ( c.getProducts().size() == 0 ) {
        catToRemove.add(c);
      }
    }
    categories.removeAll(catToRemove);

    model.addAttribute("title", "All Products");
    model.addAttribute("products", products);
    return "all-products";
  }
}
