package ch.heigvd.amt.backend.routes;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class AllProducts {

    @GetMapping("/all-products")
    @ResponseBody
    public String allProduct(Model model, @RequestParam(required = false) String category) {

        String[] products;
        if (category != null) {
            products =  new String[]{"product 1", "product 2", "product 3", "product 4"};
        } else {
            products = new String[] {"product 1", "product 2", "product 3", "product 4", "product 5", "product 6"};
        }

        String[] categories = new String[] {"cat 1", "cat 2", "cat 3", "cat 4", "cat 5", "cat 6"};
        model.addAttribute("title", "All Products");
        model.addAttribute("categories", categories);
        model.addAttribute("products", products);
        return "all-products";
    }
}
