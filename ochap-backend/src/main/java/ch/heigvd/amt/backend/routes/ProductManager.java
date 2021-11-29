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

  @GetMapping("/product-manager/all")
  public String allProduct(Model model) {
    // TODO
    Optional<List<Product>> hasProducts;
    Product[] products = new Product[] {};

    hasProducts = productDAO.getAllProducts();
    if (hasProducts.isPresent()) {
      products = hasProducts.get().toArray(new Product[0]);
    }

    return "product-management";
  }

  @GetMapping("/product-add-form")
  public String addProductForm(Model model) {
    Category[] categories = categoryDAO.getAllCategory().get().toArray(new Category[0]);
    model.addAttribute("product", new Product());
    model.addAttribute("categories", categories);

    return "product-management";
  }

  @GetMapping("/product-update-form")
  public String updateProductForm(Model model, @RequestBody Product product) {
    Category[] categories = categoryDAO.getAllCategory().get().toArray(new Category[0]);
    model.addAttribute("product", product);
    model.addAttribute("categories", categories);

    return "product-management";
  }

  @PostMapping("/product-manager/add")
  public String addProduct(@Valid Product newProduct) {
    Product p = new Product();
    p.setName(newProduct.getName());
    p.setDescription(newProduct.getDescription());
    p.setStock(newProduct.getStock());
    p.setPrice(newProduct.getPrice());
    productDAO.save(p);
    return "redirect:/all-products";
  }

  @PostMapping("/product-manager/update")
  public Product updateProduct(@RequestBody Product updatedProduct) {
    return productDAO.save(updatedProduct);
  }
}
