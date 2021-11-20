package ch.heigvd.amt.backend.entities;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Category {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column
  private String name;
}
