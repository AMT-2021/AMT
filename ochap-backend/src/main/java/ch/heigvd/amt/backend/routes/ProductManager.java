package ch.heigvd.amt.backend.routes;

import ch.heigvd.amt.backend.entities.Category;
import ch.heigvd.amt.backend.entities.Product;
import ch.heigvd.amt.backend.entities.ProductQuantity;// TODO Review NGY - unused import statement
import ch.heigvd.amt.backend.repository.CategoryDAO;
import ch.heigvd.amt.backend.repository.ProductDAO;
import ch.heigvd.amt.backend.services.HatPhotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;// TODO Review NGY - unused import
                                                                // statement
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import java.io.IOException;
import java.io.InputStream;// TODO Review NGY - unused import statement
import java.nio.file.Files;// TODO Review NGY - unused import statement
import java.nio.file.Path;// TODO Review NGY - unused import statement
import java.nio.file.Paths;// TODO Review NGY - unused import statement
import java.nio.file.StandardCopyOption;// TODO Review NGY - unused import statement
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Controller
@RolesAllowed("ROLE_ADMIN")
public class ProductManager {
  @Autowired // TODO NGY Field injection is not recommended
  private ProductDAO productDAO;

  @Autowired // TODO NGY Field injection is not recommended
  private CategoryDAO categoryDAO;

  @Autowired // TODO NGY Field injection is not recommended
  private HatPhotoService hatPhotoService;

  @GetMapping("/product-manager")
  public String allProduct(Model model) {
    model.addAttribute("products", productDAO.getAllProducts());
    return "product-management";
  }

  @GetMapping("/product-manager/add")
  public String addProductForm(Model model, @RequestParam(required = false) String error) {
    model.addAttribute("categories", categoryDAO.getAllCategories());
    model.addAttribute("product", new Product());
    model.addAttribute("route", "add");
    model.addAttribute("title", "Add new product");
    return "product-form";
  }

  @GetMapping("/product-manager/update")
  public String updateProductForm(Model model, @RequestParam Integer id,
      @RequestParam(required = false) String error) {
    if (id == null) {
      return "redirect:/product-manager";
    }
    Optional<Product> product = productDAO.getProductById(id);
    if (product.isEmpty()) {
      // The user tried to access a non-existing product.
      return "redirect:/product-manager";
    }

    model.addAttribute("categories", categoryDAO.getAllCategories());
    model.addAttribute("product", product.get());
    model.addAttribute("route", "update");
    model.addAttribute("readonlyName", true);
    model.addAttribute("title", "Update product");
    return "product-form";
  }

  @PostMapping("/product-manager/add")
  public String addProduct(@Valid Product product,
      @RequestParam(value = "categories", defaultValue = "") List<Integer> categoryIds,
      @RequestParam(required = false) MultipartFile image, BindingResult bindingResult,
      Model model) {
    for (Product p : productDAO.getAllProducts()) {
      if (p.getName().equals(product.getName())) {
        bindingResult.addError(
            new FieldError("product", "name", "A product with this name already exists."));
        model.addAttribute("route", "add");
        model.addAttribute("title", "Add new product");
        return "product-form";
      }
    }

    // TOO NGY - refactor possible with updateProduct Method
    if (image != null && !image.isEmpty()) {
      String fileName = StringUtils.cleanPath(Objects.requireNonNull(image.getOriginalFilename()));
      Product p = productDAO.save(product);

      try {
        hatPhotoService.saveHatPhoto(p.getId().toString(), image.getInputStream());
      } catch (IOException e) {
        e.printStackTrace();
      }
    } else {
      productDAO.save(product);
    }

    for (int id : categoryIds) {
      Optional<Category> c = categoryDAO.findCategoryById(id);
      c.ifPresent(cat -> {
        cat.getProducts().add(product);
        categoryDAO.save(cat);
      });
    }
    return "redirect:/product-manager";
  }

  @PostMapping("/product-manager/update")
  public String updateProduct(@Valid Product updatedProduct,
      @RequestParam(required = false) MultipartFile image, BindingResult bindingResult,
      Model model) {
    for (Product p : productDAO.getAllProducts()) {
      if (p.getName().equals(updatedProduct.getName())
          && !p.getId().equals(updatedProduct.getId())) {
        bindingResult.addError(
            new FieldError("product", "name", "A product with this name already exists."));
        model.addAttribute("route", "update");
        // Explicitly allow the user to fix the issue.
        model.addAttribute("readonlyName", false);
        model.addAttribute("title", "Update product");
        return "product-form";
      }
    }

    Optional<Product> existingProduct = productDAO.getProductById(updatedProduct.getId());
    if (existingProduct.isPresent()) {
      Product p = existingProduct.get();

      // save image and save path in product
      if (image != null && !image.isEmpty()) {
        try {
          hatPhotoService.saveHatPhoto(p.getId().toString(), image.getInputStream());
        } catch (IOException e) {
          e.printStackTrace();
        }
      }

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
