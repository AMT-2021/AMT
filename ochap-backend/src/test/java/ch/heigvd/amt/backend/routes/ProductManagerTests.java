import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.ThrowingConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import ch.heigvd.amt.backend.AmtBackendApplication;
import ch.heigvd.amt.backend.entities.Product;

@SpringBootTest(classes = AmtBackendApplication.class)
@AutoConfigureMockMvc
public class ProductManagerTests {
  @Autowired
  private MockMvc mvc;

  @Test
  @Transactional
  @WithMockUser(username = "test-user", authorities = "user")
  public void nonAdminCantManageProduct() throws Exception{
      this.mvc.perform(
              get("/product-manager")
      ).andExpect(status().is4xxClientError());
  }

  @Test
  @Transactional
  @WithMockUser(username = "test-user", authorities = "admin")
  public void adminCanManageProduct() throws Exception{
    this.mvc.perform(
            get("/product-manager")
    ).andExpect(status().is2xxSuccessful());
  }

  @Test
  @Transactional
  @WithMockUser(username = "test-user", authorities = "admin")
  public void createNewProduct() throws Exception {
    String name = "test product " + UUID.randomUUID().toString();
    String description = "Description for test product";
    String price = "10";
    String stock = "1";
    this.mvc.perform(
        post("/product-manager/add").contentType(MediaType.MULTIPART_FORM_DATA).param("name", name)
            .param("description", description).param("price", price).param("stock", stock))
        .andExpect(view().name("redirect:/product-manager"));
  }

  @Test
  @Transactional
  @WithMockUser(username = "test-user", authorities = "admin")
  public void createDuplicateProduct() throws Exception {
    String name = "test product " + UUID.randomUUID().toString();
    String description = "Description for test product";
    String price = "10";
    String stock = "1";
    this.mvc.perform(
        post("/product-manager/add").contentType(MediaType.MULTIPART_FORM_DATA).param("name", name)
            .param("description", description).param("price", price).param("stock", stock))
        .andExpect(view().name("redirect:/product-manager"));
    this.mvc
        .perform(post("/product-manager/add").contentType(MediaType.MULTIPART_FORM_DATA)
            .param("name", name).param("description", description).param("price", price)
            .param("stock", stock))
        .andExpect(content().string(containsString("A product with this name already exists.")));
  }

  @Test
  @Transactional
  @WithMockUser(username = "test-user", authorities = "admin")
  public void updateProduct() throws Exception {
    String name = "test product " + UUID.randomUUID().toString();
    String description = "Description for test product";
    String price = "10";
    String stock = "1";
    this.mvc.perform(
        post("/product-manager/add").contentType(MediaType.MULTIPART_FORM_DATA).param("name", name)
            .param("description", description).param("price", price).param("stock", stock))
        .andExpect(view().name("redirect:/product-manager"));

    findProduct(name, p -> {
      String updatedDesc = "Updated description for test product";
      p.setDescription(updatedDesc);
      this.mvc.perform(post("/product-manager/update").contentType(MediaType.MULTIPART_FORM_DATA)
          .param("id", Integer.toString(p.getId())).param("name", p.getName())
          .param("description", p.getDescription()).param("price", Integer.toString(p.getPrice()))
          .param("stock", Integer.toString(p.getStock())))
          .andExpect(view().name("redirect:/product-manager"));
      findProduct(name, updated -> {
        assertEquals(updatedDesc, updated.getDescription());
      });
    });
  }

  /**
   * Retrieves a product matching the specified name from the product list.
   *
   * This method should be called within a @Transactional. Due to the way the mvc api works, the
   * result is passed to a consumer.
   */
  private void findProduct(String name, ThrowingConsumer<Product> consumer) throws Exception {
    this.mvc.perform(get("/product-manager")).andDo(h -> {
      List<Product> prods = (List<Product>) h.getModelAndView().getModelMap().get("products");
      assertFalse(prods.isEmpty(), "All products should contain at least one product.");
      Product p = prods.stream().filter(ps -> name.equals(ps.getName())).findAny().get();
      try {
        consumer.accept(p);
      } catch (Throwable e) {
        throw new RuntimeException(e);
      }
    });
  }
}
