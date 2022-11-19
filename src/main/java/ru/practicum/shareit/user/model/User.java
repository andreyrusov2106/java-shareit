package ru.practicum.shareit.user.model;

import lombok.*;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class User {
    private Long id;
    private String name;
    private String email;

    public User(User newUser) {
        this.setId(newUser.getId());
        this.setName(newUser.getName());
        this.setEmail(newUser.getEmail());
    }
}
