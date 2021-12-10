package ch.heigvd.amt.backend.entities;

import ch.heigvd.amt.backend.repository.CategoryDAO;
import ch.heigvd.amt.backend.repository.ProductDAO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.Assertions;

import javax.transaction.Transactional;
import java.util.*;

@SpringBootTest
public class ProductTest {

  @Autowired
  private CategoryDAO categoryRepository;

  @Autowired
  private ProductDAO productRepository;


  @Test
  @Transactional
  void canCreateAProductWithoutACategory() {
    Product p = new Product();
    p.setName("Mafia");
    p.setDescription("Godfather Hat");
    p.setStock(1);
    p.setPrice(3);
    p.setCategories(new ArrayList<>());
    productRepository.save(p);
    Assertions.assertNotNull(p.getId());
    Assertions.assertNotNull(productRepository.getProductById(p.getId()).get().getCategories());
    Assertions
        .assertEquals(productRepository.getProductById(p.getId()).get().getCategories().size(), 0);
  }

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
    List<Category> cats = new ArrayList<>();
    cats.add(c);
    p.setCategories(cats);
    productRepository.save(p);
    Assertions.assertNotNull(p.getId());
    Assertions.assertNotNull(productRepository.getProductById(p.getId()).get().getCategories());
    Assertions
        .assertEquals(productRepository.getProductById(p.getId()).get().getCategories().size(), 1);
  }

  @Test
  @Transactional
  void canCreateAProductInMultipleCategories() {
    Category c = new Category();
    c.setName("Fedora");
    categoryRepository.save(c);
    Assertions.assertNotNull(c.getId());

    Category c2 = new Category();
    c2.setName("Fedora2");
    categoryRepository.save(c2);
    Assertions.assertNotNull(c2.getId());

    Category c3 = new Category();
    c3.setName("Fedora3");
    categoryRepository.save(c3);
    Assertions.assertNotNull(c3.getId());

    Product p = new Product();
    p.setName("Mafia");
    p.setDescription("Godfather Hat");
    p.setStock(1);
    p.setPrice(3);
    p.setCategories(new ArrayList<>(Arrays.asList(c, c2)));
    p.getCategories().add(c3);
    productRepository.save(p);
    Assertions.assertNotNull(p.getId());
    Assertions.assertNotNull(productRepository.getProductById(p.getId()).get().getCategories());
    Assertions
        .assertEquals(productRepository.getProductById(p.getId()).get().getCategories().size(), 3);
  }
}
