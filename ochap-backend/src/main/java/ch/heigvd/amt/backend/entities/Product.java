package ch.heigvd.amt.backend.entities;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Product {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(nullable = false)
  private String name;

  @Column
  private Integer price;

  @Column(nullable = false)
  private String description;

  @Column(nullable = false)
  private int stock;

  @Column
  private String imageRef;

  @Transient
  public String getImageRef() {
    if (imageRef == null || id == null)
      return "/uploads/default.png";

    return "/uploads/" + id + "/" + imageRef;
  }

  @ManyToMany(mappedBy = "products")
  private List<Category> categories = new ArrayList<>();
}
