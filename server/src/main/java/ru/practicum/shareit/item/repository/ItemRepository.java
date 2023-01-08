package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    @Query(" select i from Item i " +
            "where lower(i.description) like lower(concat('%', :text, '%')) and i.available=true")
    List<Item> search(String text);

    List<Item> findAllByOwnerId(Long ownerId);

    List<Item> findAllByRequestId(Long ownerId);

}
