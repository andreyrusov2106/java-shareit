package ru.practicum.shareit.user.model;

import lombok.*;

import javax.persistence.*;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users", schema = "public")
@ToString

public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "user_name", nullable = false)
    private String name;
    @Column(name = "email", unique = true)
    private String email;
}
