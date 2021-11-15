package ch.heigvd.amt.backend.repository;

import ch.heigvd.amt.backend.entities.Category;
import ch.heigvd.amt.backend.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProductDAO extends JpaRepository<Product, Integer> {
  @Query(value = "SELECT p from Product p")
  Optional <List<Product>> getAllProducts();
  Optional<Product> getProductById(Integer id);
  //Optional <List<Product>> getProductsByCategory(Category category);
}
