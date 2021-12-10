package ch.heigvd.amt.backend.routes;

import ch.heigvd.amt.backend.entities.Category;
import ch.heigvd.amt.backend.entities.Product;
import ch.heigvd.amt.backend.repository.CategoryDAO;
import ch.heigvd.amt.backend.repository.ProductDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;

@Controller
public class CategoryManager {

  @Autowired
  private CategoryDAO categoryDAO;
  @Autowired
  private ProductDAO productDAO;

  @GetMapping("/category-manager")
  public String getPage(Model model, @RequestParam(required = false) String error) {
    Category[] categories = new Category[0];
    if (categoryDAO.getAllCategory().isPresent()) {
      categories = categoryDAO.getAllCategory().get().toArray(new Category[0]);
    }
    if (error != null) {
      model.addAttribute("error", error);
    }
    model.addAttribute("categories", categories);
    model.addAttribute("newCategory", new Category());
    model.addAttribute("cat", new Category());

    return "category-manager";
  }

  @PostMapping("/category-manager/create")
  public String createCategory(@Valid Category category) {
    Category[] categories = new Category[0];
    if (categoryDAO.getAllCategory().isPresent()) {
      categories = categoryDAO.getAllCategory().get().toArray(new Category[0]);
    }
    for (int i = 0; i < categories.length; ++i) {
      if (category.getName().equals(categories[i].getName())) {
        return "redirect:/category-manager?error=1";
      }
    }
    categoryDAO.save(category);
    return "redirect:/category-manager";
  }

  @PostMapping("/category-manager/remove")
  public String removeCategory(@Valid Category c) {
    Category category = categoryDAO.getCategoryById(c.getId()).get();
    if (category.getProducts().size() != 0) {
      return "redirect:/category-manager/confirm-delete?categoryId=" + category.getId();
    } else {
      categoryDAO.delete(category);
      return "redirect:/category-manager";
    }
  }

  @PostMapping("/category-manager/remove-confirmed")
  public String removeConfirmedCategory(@Valid Category c) {
    Category category = categoryDAO.getCategoryById(c.getId()).get();
    List<Product> products = category.getProducts();
    for (Product product : products) {
      List<Category> categories = product.getCategories();
      categories.remove(category);
      product.setCategories(categories);
      productDAO.save(product); // verify if update or create
    }
    categoryDAO.delete(category);
    return "redirect:/category-manager";
  }

  @GetMapping("/category-manager/confirm-delete")
  public String confirmDelete(Model model, @RequestParam String categoryId) {
    Category category = categoryDAO.getCategoryById(Integer.parseInt(categoryId)).get();
    List<Product> products = category.getProducts();

    model.addAttribute("products", products);
    model.addAttribute("category", category);
    model.addAttribute("newCategory", new Category());
    return "confirm-category-delete";
  }
}
