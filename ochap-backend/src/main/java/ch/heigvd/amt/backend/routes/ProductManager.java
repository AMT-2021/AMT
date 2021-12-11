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

/*
 * routes pour : - affichage de tous les produits (GET) - btn update, delete, add qui redirige sur
 * les bons trucs - affichage update (GET) - affichage add (même template que update) (GET) - une
 * route pour modif d'un produit (POST) - une route pour add (POST) - une route pour delete (POST)
 *
 */

@Controller
public class ProductManager {
  @Autowired
  private ProductDAO productDAO;

  @Autowired
  private CategoryDAO categoryDAO;

  @GetMapping("/product-manager")
  public String allProduct(Model model) {
    // TODO
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
  public String addProductForm(Model model) {
    Optional<List<Category>> cats = categoryDAO.getAllCategory();
    cats.ifPresent(categories -> model.addAttribute("categories", categories));
    model.addAttribute("product", new Product());
    model.addAttribute("route", "add");
    return "product-form";
  }

  @PostMapping("/product-update-form")
  public String updateProductForm(Model model, @Valid Product product) {
    Optional<List<Category>> cats = categoryDAO.getAllCategory();
    cats.ifPresent(categories -> model.addAttribute("categories", categories));
    model.addAttribute("product", product);
    model.addAttribute("route", "update");
    return "product-form";
  }

  // TODO: Do all operations between Product and Category from the Category table
  @PostMapping("/product-manager/add")
  public String addProduct(@Valid Product newProduct,
      @RequestParam(value = "categories") int[] categoriesId) {
    List<Category> categories = new ArrayList<>();
    productDAO.save(newProduct);
    for (int id : categoriesId) {
      Optional<Category> c = categoryDAO.findCategoryById(id);
      /*
       * c.ifPresent(categories::add); newProduct.setCategories(categories);
       */
      c.ifPresent(cat -> {
        cat.getProducts().add(newProduct);
        categoryDAO.save(cat);
      });
    }
    return "redirect:/product-manager";
  }

  @PostMapping("/product-manager/update")
  public String updateProduct(@Valid Product updatedProduct) {
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
