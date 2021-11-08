package ch.heigvd.amt.backend.repository;

import ch.heigvd.amt.backend.DBSchema.Category;
import ch.heigvd.amt.backend.DBSchema.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryDAO extends JpaRepository<Category, Integer> {
}
