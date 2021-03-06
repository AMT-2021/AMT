package ch.heigvd.amt.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import ch.heigvd.amt.backend.entities.Product;

public interface ProductDAO extends JpaRepository<Product, Integer> {
  @Query(value = "SELECT p from Product p")
  List<Product> getAllProducts();

  Optional<Product> getProductById(Integer id);

  @Query(value = "SELECT p FROM Product p " + "JOIN p.categories c " + "WHERE c.id = ?1")
  List<Product> getProductsByCategoryId(int id);

  @Override
  void deleteById(Integer integer);
}
