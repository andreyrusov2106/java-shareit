package ru.practicum.shareit.booking.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

public interface BookingRepository extends PagingAndSortingRepository<Booking, Long> {
    Page<Booking> findBookingsByBooker(User booker, Pageable pageable);

    Page<Booking> findAllByBookerAndStartIsBeforeAndEndIsAfter(User booker, LocalDateTime dateTime, LocalDateTime dateTime1, Pageable pageable);

    Page<Booking> findAllByBookerAndEndIsBeforeAndStatusIs(User booker, LocalDateTime dateTime, Status status, Pageable pageable);

    Page<Booking> findAllByBookerAndStartIsAfter(User booker, LocalDateTime dateTime, Pageable pageable);

    Page<Booking> findAllByBookerAndStatusIs(User booker, Status status, Pageable pageable);

    @Query(" select b from Booking b " +
            "where b.item.owner.id =:ownerId")
    Page<Booking> findAllByOwnerOfItem(Long ownerId, Pageable pageable);

    @Query(" select b from Booking b " +
            "where (b.item.owner =:owner " +
            "and (:dateTime between b.start and b.end))")
    Page<Booking> findAllByOwnerOfItemAndStartIsBeforeAndEndIsAfter(User owner, LocalDateTime dateTime, Pageable pageable);

    @Query(" select b from Booking b " +
            "where b.item.owner.id =:ownerId " +
            " and b.end<:dateTime " +
            "and b.status=:status")
    Page<Booking> findAllByOwnerOfItemAndEndIsBeforeAndStatusIs(Long ownerId, LocalDateTime dateTime, Status status, Pageable pageable);

    @Query(" select b from Booking b " +
            "where b.item.owner.id =:ownerId " +
            "and b.start>:dateTime")
    Page<Booking> findAllByOwnerOfItemAndStartIsAfter(Long ownerId, LocalDateTime dateTime, Pageable pageable);

    @Query(" select b from Booking b " +
            "where b.item.owner.id =:ownerId " +
            "and b.status = :status")
    Page<Booking> findAllByOwnerOfItemAndStatusIs(Long ownerId, Status status, Pageable pageable);

    Booking findTop1ByItemAndEndIsBeforeOrderByEndDesc(Item item, LocalDateTime now);

    Booking findTop1ByItemAndStartIsAfterOrderByStartDesc(Item item, LocalDateTime now);

    Booking findTop1ByItemAndBookerAndEndIsBefore(Item item, User user, LocalDateTime now);
}
