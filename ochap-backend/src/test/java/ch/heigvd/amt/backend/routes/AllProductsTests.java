package ch.heigvd.amt.backend.routes;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class AllProductsTests {
    @Autowired
    private MockMvc mvc;

    @Test
    public void allProductsPageIsOk() throws Exception {
        mvc.perform(get("/all-products")).andExpect(status().isOk());
    }

    @Test
    public void DisplayNoAvailableProductForASpecificCategory() throws Exception {
        mvc.perform(get("/all-products?category=-1"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("No article available with this category")));
    }
}
