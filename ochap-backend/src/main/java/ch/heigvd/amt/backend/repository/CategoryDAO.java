package ch.heigvd.amt.backend.repository;

import ch.heigvd.amt.backend.entities.Category;
import ch.heigvd.amt.backend.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CategoryDAO extends JpaRepository<Category, Integer> {
  @Query(value = "SELECT p from Category p")
  Optional<List<Category>> getAllCategory();
    Optional<Category> getCategoryById(Integer id);
}