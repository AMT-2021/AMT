package ch.heigvd.amt.backend.routes;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ShoppingCartTemplate {
  @GetMapping("/shopping-cart")
  public String basicTemplate(Model model) {
    Item[] items = new Item[] {new Item("p1", 1, 5.2f), new Item("p2", 2, 3.4f)};
    model.addAttribute("items", items);
    return "shopping-cart-template";
  }


class Item {
  public String name;
  public int number;
  public float price;

  public Item(String name, int number, float price) {
    this.name = name;
    this.number = number;
    this.price = price;
  }
}
