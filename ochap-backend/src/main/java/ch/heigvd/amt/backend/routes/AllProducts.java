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


@Controller
public class AllProducts {

  @Autowired
  private ProductDAO productDAO;

  @Autowired
  private CategoryDAO categoryDAO;

  @GetMapping("/all-products")
  public String allProduct(Model model, @RequestParam(required = false) Integer category) {
    List<Product> products = null;
    if (category != null) {
      var c = categoryDAO.getCategoryById(category);
      if (c.isPresent()) {
        products = c.get().getProducts();
      }
    }

    if (products == null) {
      products = productDAO.getAllProducts();
    }

    List<Category> categories = categoryDAO.getAllCategories();
    List<Category> catToRemove = new ArrayList<Category>();
    for (Category c : categories) {
      if (c.getProducts().size() == 0) {
        catToRemove.add(c);
      }
    }
    categories.removeAll(catToRemove);

    model.addAttribute("title", "All Products");
    model.addAttribute("categories", categories);
    model.addAttribute("currentCategory", category);
    model.addAttribute("products", products);
    return "all-products";
  }
}
