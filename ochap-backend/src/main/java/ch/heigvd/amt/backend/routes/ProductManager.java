package ch.heigvd.amt.backend.routes;

import ch.heigvd.amt.backend.entities.Category;
import ch.heigvd.amt.backend.entities.Product;
import ch.heigvd.amt.backend.entities.ProductQuantity;
import ch.heigvd.amt.backend.repository.CategoryDAO;
import ch.heigvd.amt.backend.repository.ProductDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
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
    Category[] categories = categoryDAO.getAllCategory().get().toArray(new Category[0]);
    model.addAttribute("product", new Product());
    model.addAttribute("categories", categories);

    return "product-form";
  }

  @GetMapping("/product-update-form")
  public String updateProductForm(Model model, @RequestBody Product product) {
    Category[] categories = categoryDAO.getAllCategory().get().toArray(new Category[0]);
    model.addAttribute("product", product);
    model.addAttribute("categories", categories);

    return "product-form";
  }

  @PostMapping("/product-manager/add")
  public String addProduct(@Valid Product newProduct) {
    Product p = new Product();
    productDAO.save(assignProduct(p, newProduct));
    return "redirect:/product-manager";
  }

  @PostMapping("/product-manager/update")
  public String updateProduct(@RequestBody Product updatedProduct) {
    Optional<Product> existingProduct = productDAO.getProductById(updatedProduct.getId());
    if (!existingProduct.isEmpty()) {
      Product p = existingProduct.get();
      productDAO.save(assignProduct(p, updatedProduct));
    }
    return "redirect:/product-manager";
  }

  public Product assignProduct(Product p1, Product p2) {
    p1.setName(p2.getName());
    p1.setDescription(p2.getDescription());
    p1.setStock(p2.getStock());
    p1.setPrice(p2.getPrice());
    p1.setCategory(p2.getCategory());
    return p1;
  }

  @PostMapping(path = "/product-manager/remove")
  public String removeProduct(@Valid Product productToRemove) {
    // TODO
    return "redirect:/product-manager";
  }

}
