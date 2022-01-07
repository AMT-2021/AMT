package ch.heigvd.amt.backend.entities;

import lombok.Data;
import lombok.NonNull;//TODO NGY - Unused import statement

import javax.persistence.*;

@Entity
@Data
public class ProductQuantity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @ManyToOne(optional = false, cascade = CascadeType.PERSIST)
  private Product product;

  @ManyToOne(optional = false, cascade = CascadeType.PERSIST)
  private ShoppingCart shoppingCart;

  @Column(nullable = false)
  private Integer quantity;
}
