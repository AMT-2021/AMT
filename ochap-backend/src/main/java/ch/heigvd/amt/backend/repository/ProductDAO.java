package ch.heigvd.amt.backend.repository;

import ch.heigvd.amt.backend.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductDAO extends JpaRepository<Product, Integer> {
  Optional<Product> getProductById(Integer id);
}
