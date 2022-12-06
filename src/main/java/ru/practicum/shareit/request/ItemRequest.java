package ru.practicum.shareit.request;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.time.LocalDate;

/**
 * TODO Sprint add-item-requests.
 */
@Data
@Entity
@Getter
@Setter
@ToString
@Table(name = "requests", schema = "public")
public class ItemRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String description;
    @OneToOne()
    @JoinColumn(name = "requester_id", referencedColumnName = "id")
    private User requester;
    private LocalDate created;
}
