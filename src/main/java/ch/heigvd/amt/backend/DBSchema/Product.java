package ch.heigvd.amt.backend.DBSchema;

import lombok.*;

import javax.persistence.*;

@Entity
@Data
public class Product {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Integer id;


  @Column
  private String name;

  @Column
  private int price;


  @Column
  private String description;


  @Column
  private int stock;

  @ManyToOne
  @JoinColumn(name = "category_id")
  private Category category;
}
