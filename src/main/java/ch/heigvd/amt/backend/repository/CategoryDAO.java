package ch.heigvd.amt.backend.repository;

import ch.heigvd.amt.backend.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryDAO extends JpaRepository<Category, Integer> {
}
