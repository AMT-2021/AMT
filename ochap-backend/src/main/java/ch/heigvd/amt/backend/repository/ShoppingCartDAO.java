package ch.heigvd.amt.backend.repository;

import ch.heigvd.amt.backend.entities.Product;// TODO Review NGY - unused import statement
import ch.heigvd.amt.backend.entities.ShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ShoppingCartDAO extends JpaRepository<ShoppingCart, Integer> {
  @Query(value = "SELECT sc FROM ShoppingCart sc WHERE sc.clientId = ?1")
  Optional<ShoppingCart> findByClientId(Integer clientId);

  Optional<ShoppingCart> getShoppingCartById(Integer id);
}
