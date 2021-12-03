package ch.heigvd.amt.backend.entities;

import ch.heigvd.amt.backend.repository.CategoryDAO;
import ch.heigvd.amt.backend.repository.ProductDAO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.Assertions;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@SpringBootTest
public class ProductTest {

  @Autowired
  private CategoryDAO categoryRepository;

  @Autowired
  private ProductDAO productRepository;

  @Test
  @Transactional
  void canCreateAProductInACategory() {
    Category c = new Category();
    c.setName("Fedora");
    categoryRepository.save(c);
    Assertions.assertNotNull(c.getId());
    Product p = new Product();
    p.setName("Mafia");
    p.setDescription("Godfather Hat");
    p.setStock(1);
    p.setPrice(3);
    p.setCategory(c);
    productRepository.save(p);
    Assertions.assertNotNull(p.getId());
    Assertions.assertNotNull(productRepository.getProductById(p.getId()).get().getCategories());
    Assertions.assertEquals(productRepository.getProductById(p.getId()).get().getCategories().size(), 1);
  }
}
