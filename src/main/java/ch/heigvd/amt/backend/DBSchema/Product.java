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
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Getter
    private Integer id;

    @Getter
    @Setter
    private String name;

    @Getter
    @Setter
    private int price;

    @Getter
    @Setter
    private String description;

    @Getter
    @Setter
    private int stock;
}
