package ch.heigvd.amt.backend;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@SpringBootTest
@AutoConfigureMockMvc
public class BasicTemplateTests {
  @Autowired private MockMvc mvc;

  @Test
  public void basicTemplate() throws Exception {
    mvc.perform(MockMvcRequestBuilders.get("/basic-template"))
        .andExpect(status().isOk())
        .andExpect(content().string(containsString("Basic template")));
  }
}
