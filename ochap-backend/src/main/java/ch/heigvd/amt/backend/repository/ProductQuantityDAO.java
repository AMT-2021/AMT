package ch.heigvd.amt.backend.repository;

import ch.heigvd.amt.backend.entities.ProductQuantity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProductQuantityDAO extends JpaRepository<ProductQuantity, Integer> {

    @Query(value = "SELECT p from ProductQuantity p WHERE p.shoppingCart.id = ?1")
    Optional<List<ProductQuantity>> findByShoppingCartId(Integer shoppingCartId);

    Optional<ProductQuantity> getProductQuantityById(Integer id);

    @Override
    void delete(ProductQuantity entity);
}
