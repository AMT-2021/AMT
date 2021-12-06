package ch.heigvd.amt.backend.repository;

import ch.heigvd.amt.backend.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryDAO extends JpaRepository<Category, Integer> {
  @Query(value = "SELECT p from Category p")
  Optional<List<Category>> getAllCategory();

  Optional<Category> getCategoryById(Integer id);
  Optional<Category> findCategoryById(Integer id);
  @Override
  void delete(Category category);
}
