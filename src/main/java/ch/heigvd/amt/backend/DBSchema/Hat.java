package ch.heigvd.amt.backend.DBSchema;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@ToString
@EqualsAndHashCode
public class Hat {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Getter
  private Integer id;

  @Getter
  @Setter
  private String name;
++ b/src/main/java/ch/heigvd/amt/backend/routes/HatService.java
}
