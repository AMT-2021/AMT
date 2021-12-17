package ch.heigvd.amt.backend.routes;

import ch.heigvd.amt.backend.AmtBackendApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = AmtBackendApplication.class)
@AutoConfigureMockMvc
public class CategoryManagerTest {
    @Autowired
    private MockMvc mvc;

    @Test
    @Transactional
    @WithMockUser(username = "test-user", authorities = "user")
    public void nonAdminCantManageCategory() throws Exception{
        this.mvc.perform(
                get("/category-manager")
        ).andExpect(status().is4xxClientError());
    }

    @Test
    @Transactional
    @WithMockUser(username = "test-user", authorities = "admin")
    public void adminCanManageProduct() throws Exception{
        this.mvc.perform(
                get("/category-manager")
        ).andExpect(status().is2xxSuccessful());
    }
}
