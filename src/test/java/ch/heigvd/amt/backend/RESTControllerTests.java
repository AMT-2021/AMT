package ch.heigvd.amt.backend;

import ch.heigvd.amt.backend.repository.HatDAO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import javax.transaction.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class RESTControllerTests {
  @Autowired private MockMvc mockMvc;

  @Autowired private HatDAO hatRepository;

  @Test
  @Transactional
  public void createAndSelectHatTest() throws Exception {
    this.mockMvc
        .perform(post("/hats").contentType(MediaType.APPLICATION_JSON)
            .content("{ \"name\": \"testHat\" }").accept(MediaType.APPLICATION_JSON))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.name").value("testHat"));
  }
}
