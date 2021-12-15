import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import ch.heigvd.amt.backend.AmtBackendApplication;

@SpringBootTest(classes = AmtBackendApplication.class)
@AutoConfigureMockMvc
public class ProductManagerTests {
  @Autowired
  private MockMvc mvc;

  @Test
  @WithMockUser(username = "test-user", roles = "admin")
  public void createNewProduct() throws Exception {
    String name = "new product " + UUID.randomUUID().toString();
    String description = "Description for new product";
    String price = "10";
    String stock = "1";
    this.mvc.perform(
        post("/product-manager/add").contentType(MediaType.MULTIPART_FORM_DATA).param("name", name)
            .param("description", description).param("price", price).param("stock", stock))
        .andExpect(view().name("redirect:/product-manager"));
  }
}
