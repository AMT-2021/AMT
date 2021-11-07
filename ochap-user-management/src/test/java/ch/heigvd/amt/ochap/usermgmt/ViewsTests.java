package ch.heigvd.amt.ochap.usermgmt;

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
public class ViewsTests {
  @Autowired
  private MockMvc mvc;

  @Test
  public void testOupsPage() throws Exception {
    mvc.perform(MockMvcRequestBuilders.get("/test-oups-page")).andExpect(status().isOk())
        .andExpect(content().string(containsString("Test oups page.")))
        .andExpect(content().string(containsString("working on it.")))
        .andExpect(content().string(containsString("form action=\"/\"")))
        .andExpect(content().string(containsString("type=\"submit\" value=\"Go back\"")));
  }
}
