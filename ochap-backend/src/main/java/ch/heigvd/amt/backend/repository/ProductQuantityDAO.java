package ch.heigvd.amt.backend.repository;

import ch.heigvd.amt.backend.entities.ProductQuantity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductQuantityDAO extends JpaRepository<ProductQuantity, Integer> {
}
