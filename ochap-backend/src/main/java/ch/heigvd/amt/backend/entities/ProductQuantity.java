package ch.heigvd.amt.backend.entities;

import lombok.Data;
import lombok.NonNull;

import javax.persistence.*;

@Entity
@Data
public class ProductQuantity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @OneToOne(optional = false, cascade = CascadeType.PERSIST)
  private Product product;

  @ManyToOne(optional = false, cascade = CascadeType.PERSIST)
  private ShoppingCart shoppingCart;

  @Column(nullable = false)
  private Integer quantity;
}
