package ru.practicum.shareit.item.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.model.User;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class Item {

    private Long id;
    @NotNull
    @NotBlank
    private String name;
    @NotNull
    @NotBlank
    private String description;
    @NotNull
    private Boolean available;
    private User owner;
    private ItemRequest request;

    public Item(Item newItem) {
        this.setId(newItem.getId());
        this.setName(newItem.getName());
        this.setDescription(newItem.getDescription());
        this.setAvailable(newItem.getAvailable());
        this.setOwner(newItem.getOwner());
        this.setRequest(newItem.getRequest());
    }
}
