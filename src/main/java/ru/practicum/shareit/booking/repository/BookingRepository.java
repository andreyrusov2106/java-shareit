package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findBookingsByBooker(User booker);

    List<Booking> findAllByBookerAndStartIsBeforeAndEndIsAfter(User booker, LocalDateTime dateTime, LocalDateTime dateTime1);

    List<Booking> findAllByBookerAndEndIsBeforeAndStatusIs(User booker, LocalDateTime dateTime, Status status);

    List<Booking> findAllByBookerAndStartIsAfter(User booker, LocalDateTime dateTime);

    List<Booking> findAllByBookerAndStatusIs(User booker, Status status);

    @Query(" select b from Booking b " +
            "where b.item.owner.id =?1")
    List<Booking> findAllByOwnerOfItem(Long ownerId);

    @Query(" select b from Booking b " +
            "where (b.item.owner =?1 " +
            "and ( b.start < ?2 and b.end>?2))")
    List<Booking> findAllByOwnerOfItemAndStartIsBeforeAndEndIsAfter(User owner, LocalDateTime dateTime1);

    @Query(" select b from Booking b " +
            "where b.item.owner.id =?1 " +
            " and b.end<?2 " +
            "and b.status=?3")
    List<Booking> findAllByOwnerOfItemAndEndIsBeforeAndStatusIs(Long ownerId, LocalDateTime dateTime1, Status status);

    @Query(" select b from Booking b " +
            "where b.item.owner.id =?1 " +
            "and b.start>?2")
    List<Booking> findAllByOwnerOfItemAndStartIsAfter(Long ownerId, LocalDateTime dateTime1);

    @Query(" select b from Booking b " +
            "where b.item.owner.id =?1 " +
            "and b.status = ?2")
    List<Booking> findAllByOwnerOfItemAndStatusIs(Long ownerId, Status status);

    Booking findTop1ByItemAndEndIsBeforeOrderByEndDesc(Item item, LocalDateTime now);

    Booking findTop1ByItemAndStartIsAfterOrderByStartDesc(Item item, LocalDateTime now);

    Booking findTop1ByItemAndBookerAndEndIsBefore(Item item, User user, LocalDateTime now);
}
