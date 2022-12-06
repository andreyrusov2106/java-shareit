package ru.practicum.shareit.item.model;

import lombok.*;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "comments", schema = "public")
@Getter
@Setter
@ToString
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotEmpty
    @Column(name = "text")
    private String text;
    @OneToOne()
    @JoinColumn(name = "item_id", referencedColumnName = "id")
    private Item item;
    @OneToOne()
    @JoinColumn(name = "author_id", referencedColumnName = "id")
    private User author;
    @Column(name = "created_date")
    private LocalDateTime created;
}
