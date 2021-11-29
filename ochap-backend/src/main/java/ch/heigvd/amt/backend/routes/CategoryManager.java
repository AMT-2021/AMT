package ch.heigvd.amt.backend.routes;

import ch.heigvd.amt.backend.entities.Category;
import ch.heigvd.amt.backend.entities.ProductQuantity;
import ch.heigvd.amt.backend.repository.CategoryDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Controller
public class CategoryManager {

    @Autowired
    private CategoryDAO categoryDAO;

    @GetMapping("/category-manager")
    public String getPage(Model model) {
        Category[] categories = new Category[0];
        if(categoryDAO.getAllCategory().isPresent()) {
            categories = categoryDAO.getAllCategory().get().toArray(new Category[0]);
        }
        model.addAttribute("categories", categories);
        model.addAttribute("newCategory", new Category());
        model.addAttribute("cat", new Category());

        return "category-manager";
    }

    @PostMapping("/category-manager/create")
    public String createCategory(@Valid Category category) {
        categoryDAO.save(category);
        return "redirect:/category-manager";
    }

    @PostMapping("/category-manager/remove")
    public String removeCategory(@Valid Category category) {
        Optional<Category> cat = categoryDAO.getCategoryById(category.getId());
        cat.ifPresent(entity -> categoryDAO.delete(entity));
        return "redirect:/category-manager";
    }
}
