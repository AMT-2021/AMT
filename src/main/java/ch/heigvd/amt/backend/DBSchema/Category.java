package ch.heigvd.amt.backend.DBSchema;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Category {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "category_id")
  private Integer id;

  @Column
  private String name;
}
