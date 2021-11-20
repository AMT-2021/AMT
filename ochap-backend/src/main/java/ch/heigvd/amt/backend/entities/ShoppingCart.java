package ch.heigvd.amt.backend.entities;

import lombok.Data;

import javax.persistence.*;
import java.util.Set;

@Entity
@Data
public class ShoppingCart {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(unique = true)
  private Integer clientId;

  @OneToMany(mappedBy = "shoppingCart", cascade = CascadeType.ALL)
  private Set<ProductQuantity> products;
}
