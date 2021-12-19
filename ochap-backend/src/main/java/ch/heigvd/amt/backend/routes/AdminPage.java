package ch.heigvd.amt.backend.routes;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.annotation.security.RolesAllowed;

@Controller
@RolesAllowed("ROLE_ADMIN")
public class AdminPage {

    @GetMapping("/admin")
    public String getPage(Model model){
        return "admin-page";
    }
}
