package ch.heigvd.amt.backend.entities;

import lombok.*;

import javax.persistence.*;

@Entity
@Data
public class Product {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private int price;

  @Column(nullable = false)
  private String description;

  @Column(nullable = false)
  private int stock;

  @ManyToOne
  private Category category;
}
