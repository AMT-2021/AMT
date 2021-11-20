package ch.heigvd.amt.backend;

import ch.heigvd.amt.backend.entities.Category;
import ch.heigvd.amt.backend.entities.Product;
import ch.heigvd.amt.backend.entities.ProductQuantity;
import ch.heigvd.amt.backend.entities.ShoppingCart;
import ch.heigvd.amt.backend.repository.CategoryDAO;
import ch.heigvd.amt.backend.repository.ProductDAO;
import ch.heigvd.amt.backend.repository.ProductQuantityDAO;
import ch.heigvd.amt.backend.repository.ShoppingCartDAO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.Set;

@SpringBootTest
@Transactional
public class ShoppingCartTest {

  @Autowired
  private CategoryDAO categoryRepository;

  @Autowired
  private ProductDAO productRepository;

  @Autowired
  private ShoppingCartDAO shoppingCartRepository;

  @Autowired
  private ProductQuantityDAO productQuantityRepository;

  @Autowired
  private EntityManager em;

  @Test
  void canCreateAShoppingCartWithProductQuantity() {

    Category c = new Category();
    c.setName("Fedora");
    categoryRepository.save(c);

    Product p1 = new Product();
    p1.setName("Mafia");
    p1.setDescription("Godfather Hat");
    p1.setStock(1);
    p1.setPrice(3);
    p1.setCategory(c);
    productRepository.save(p1);

    Product p2 = new Product();
    p2.setName("Mafia2");
    p2.setDescription("Godfather Hat2");
    p2.setStock(1);
    p2.setPrice(3);
    p2.setCategory(c);
    productRepository.save(p2);

    ShoppingCart sC = new ShoppingCart();
    sC.setClientId(1);
    shoppingCartRepository.save(sC);

    ProductQuantity pQ1 = new ProductQuantity();
    pQ1.setProduct(p1);
    pQ1.setQuantity(2);
    pQ1.setShoppingCart(sC);
    productQuantityRepository.save(pQ1);

    System.out.println("pQ1 sC: " + pQ1.getShoppingCart());
    ProductQuantity pQ2 = new ProductQuantity();
    pQ2.setProduct(p2);
    pQ2.setQuantity(2);
    pQ2.setShoppingCart(sC);
    productQuantityRepository.save(pQ2);

    shoppingCartRepository.flush(); // Persist this entity.
    em.detach(sC); // Discard this entity's cache.
    sC = shoppingCartRepository.getById(sC.getId());

    Assertions.assertNotNull(sC.getId());
    Assertions.assertNotNull(sC.getProducts());
    Assertions.assertEquals(2, sC.getProducts().size());
  }
}
