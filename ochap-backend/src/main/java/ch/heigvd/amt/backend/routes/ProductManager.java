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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class ProductManager {
  @Autowired
  private ProductDAO productDAO;

  @Autowired
  private CategoryDAO categoryDAO;

  @GetMapping("/product-manager")
  public String allProduct(Model model) {
    Optional<List<Product>> hasProducts;
    Product[] products = new Product[] {};

    hasProducts = productDAO.getAllProducts();
    if (hasProducts.isPresent()) {
      products = hasProducts.get().toArray(new Product[0]);
    }
    model.addAttribute("products", products);
    return "product-management";
  }

  @GetMapping("/product-add-form")
  public String addProductForm(Model model, @RequestParam(required = false) String error) {
    Optional<List<Category>> cats = categoryDAO.getAllCategory();
    cats.ifPresent(categories -> model.addAttribute("categories", categories));
    if (error != null) {
      model.addAttribute("error", error);
    }
    model.addAttribute("product", new Product());
    model.addAttribute("route", "add");
    return "product-form";
  }

  @GetMapping("/product-update-form")
  public String updateProductForm(Model model, @RequestParam String id, @RequestParam(required = false) String error) {
    Product product = productDAO.getProductById(Integer.parseInt(id)).get();
    Optional<List<Category>> cats = categoryDAO.getAllCategory();
    cats.ifPresent(categories -> model.addAttribute("categories", categories));
    if (error != null) {
      model.addAttribute("error", error);
    }
    model.addAttribute("product", product);
    model.addAttribute("route", "update");
    return "product-form";
  }

  @PostMapping("/product-manager/add")
  public String addProduct(@Valid Product newProduct,
      @RequestParam(value = "categories") int[] categoriesId) {
    List<Product> allProducts = productDAO.getAllProducts().get();
    for (Product p : allProducts) {
      if (p.getName().equals(newProduct.getName())) {
        return "redirect:/product-add-form?error=1";
      }
    }
    List<Category> categories = new ArrayList<>();
    productDAO.save(newProduct);
    for (int id : categoriesId) {
      Optional<Category> c = categoryDAO.findCategoryById(id);
      c.ifPresent(cat -> {
        cat.getProducts().add(newProduct);
        categoryDAO.save(cat);
      });
    }
    return "redirect:/product-manager";
  }

  @PostMapping("/product-manager/update")
  public String updateProduct(@Valid Product updatedProduct) {
    List<Product> allProducts = productDAO.getAllProducts().get();
    for (Product p : allProducts) {
      if (p.getName().equals(updatedProduct.getName())) {
        return "redirect:/product-update-form?error=1&id=" + updatedProduct.getId();
      }
    }
    Optional<Product> existingProduct = productDAO.getProductById(updatedProduct.getId());
    if (existingProduct.isPresent()) {
      Product p = existingProduct.get();
      productDAO.save(assignProduct(p, updatedProduct));

      // Clear the product of all categories
      for (Category existingCat : p.getCategories()) {
        existingCat.getProducts().remove(p);
        categoryDAO.save(existingCat);
      }

      // add news
      for (Category newCat : updatedProduct.getCategories()) {
        newCat.getProducts().add(p);
        categoryDAO.save(newCat);
      }
    }
    return "redirect:/product-manager";
  }

  public Product assignProduct(Product p1, Product p2) {
    p1.setName(p2.getName());
    p1.setDescription(p2.getDescription());
    p1.setStock(p2.getStock());
    p1.setPrice(p2.getPrice());
    return p1;
  }

  @PostMapping(path = "/product-manager/remove")
  public String removeProduct(@Valid Product productToRemove) {
    Optional<Product> existingProduct = productDAO.getProductById(productToRemove.getId());
    if (existingProduct.isPresent()) {
      Product p = existingProduct.get();
      for (Category existingCat : p.getCategories()) {
        existingCat.getProducts().remove(p);
        categoryDAO.save(existingCat);
      }
      productDAO.deleteById(p.getId());
    }
    return "redirect:/product-manager";
  }
}
