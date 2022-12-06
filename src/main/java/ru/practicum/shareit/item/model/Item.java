package ru.practicum.shareit.item.model;

import lombok.*;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "items", schema = "public")
@Getter
@Setter
@ToString
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "item_name", nullable = false)
    private String name;
    @Column(name = "description")
    private String description;
    @Column(name = "is_available")
    private Boolean available;
    @OneToOne()
    @JoinColumn(name = "owner_id", referencedColumnName = "id")
    private User owner;
    @OneToOne()
    @JoinColumn(name = "request_id", referencedColumnName = "id")
    private ItemRequest request;

    public void Item() {

    }

    public Item(Item newItem) {
        this.setId(newItem.getId());
        this.setName(newItem.getName());
        this.setDescription(newItem.getDescription());
        this.setAvailable(newItem.getAvailable());
        this.setOwner(newItem.getOwner());
        this.setRequest(newItem.getRequest());
    }
}
