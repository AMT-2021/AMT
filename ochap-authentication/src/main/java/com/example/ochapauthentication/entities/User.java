package com.example.ochapauthentication.entities;

import lombok.*;

import javax.persistence.*;

@Data
@Entity
@Table(name = "users")
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(nullable = false, unique = true)
  private String username;

  @Column(nullable = false)
  private byte[] passwordHash;

  @Column(nullable = false)
  private byte[] salt;

  @ManyToOne(optional = false, cascade = CascadeType.PERSIST)
  private Role role;


}
