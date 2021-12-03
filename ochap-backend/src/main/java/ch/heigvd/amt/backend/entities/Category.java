package ch.heigvd.amt.backend.entities;

import lombok.Data;

import javax.persistence.*;
import java.util.Set;

@Data
@Entity
public class Category {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column
  private String name;

  @ManyToMany
  @JoinTable(name="product_category",
          joinColumns=@JoinColumn(name="category_id"),
          inverseJoinColumns=@JoinColumn(name="product_id")
  )
  private Set<Product> products;
}
