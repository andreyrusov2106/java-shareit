package ru.practicum.shareit.booking.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends PagingAndSortingRepository<Booking, Long> {
    List<Booking> findBookingsByBooker(User booker, Pageable pageable);

    List<Booking> findAllByBookerAndStartIsBeforeAndEndIsAfter(User booker, LocalDateTime dateTime, LocalDateTime dateTime1, Pageable pageable);

    List<Booking> findAllByBookerAndEndIsBeforeAndStatusIs(User booker, LocalDateTime dateTime, Status status, Pageable pageable);

    List<Booking> findAllByBookerAndStartIsAfter(User booker, LocalDateTime dateTime, Pageable pageable);

    List<Booking> findAllByBookerAndStatusIs(User booker, Status status, Pageable pageable);

    @Query(" select b from Booking b " +
            "where b.item.owner.id =:ownerId")
    List<Booking> findAllByOwnerOfItem(Long ownerId, Pageable pageable);

    @Query(" select b from Booking b " +
            "where (b.item.owner =:owner " +
            "and (:dateTime between b.start and b.end))")
    List<Booking> findAllByOwnerOfItemAndStartIsBeforeAndEndIsAfter(User owner, LocalDateTime dateTime, Pageable pageable);

    @Query(" select b from Booking b " +
            "where b.item.owner.id =:ownerId " +
            " and b.end<:dateTime " +
            "and b.status=:status")
    List<Booking> findAllByOwnerOfItemAndEndIsBeforeAndStatusIs(Long ownerId, LocalDateTime dateTime, Status status, Pageable pageable);

    @Query(" select b from Booking b " +
            "where b.item.owner.id =:ownerId " +
            "and b.start>:dateTime")
    List<Booking> findAllByOwnerOfItemAndStartIsAfter(Long ownerId, LocalDateTime dateTime, Pageable pageable);

    @Query(" select b from Booking b " +
            "where b.item.owner.id =:ownerId " +
            "and b.status = :status")
    List<Booking> findAllByOwnerOfItemAndStatusIs(Long ownerId, Status status, Pageable pageable);

    Booking findTop1ByItemAndEndIsBeforeOrderByEndDesc(Item item, LocalDateTime now);

    Booking findTop1ByItemAndStartIsAfterOrderByStartDesc(Item item, LocalDateTime now);

    Booking findTop1ByItemAndBookerAndEndIsBefore(Item item, User user, LocalDateTime now);
}
