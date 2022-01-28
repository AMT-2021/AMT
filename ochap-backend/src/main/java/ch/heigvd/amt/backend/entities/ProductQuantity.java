package ch.heigvd.amt.backend.entities;

import javax.persistence.*;

import lombok.Data;

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
