package ch.heigvd.amt.backend.entities;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
public class ShoppingCart {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(unique = true)
  private Integer clientId;

  @OneToMany(mappedBy = "shoppingCart", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  private List<ProductQuantity> products;
}
