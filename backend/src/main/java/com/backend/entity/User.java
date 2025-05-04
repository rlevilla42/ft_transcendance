package com.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.ToString;
import lombok.Builder;
import lombok.AccessLevel;
import lombok.Setter;


@Data
@ToString(exclude = "password")
@Entity //Spécifie le nom de la table SQL associée à cette entité. Sans ça, JPA aurait pris le nom de la classe (User) comme nom de table par défaut. Ici, tu lui dis "non, appelle-la users dans la BDD"
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = true)
    private String password;

    public User() {};
    @Builder
    public User(Long id, String username, String password) {};
}
