package ch.heigvd.amt.backend.repository;

import ch.heigvd.amt.backend.entities.Product;
import ch.heigvd.amt.backend.entities.ShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShoppingCartDAO extends JpaRepository<ShoppingCart, Integer> {
}
