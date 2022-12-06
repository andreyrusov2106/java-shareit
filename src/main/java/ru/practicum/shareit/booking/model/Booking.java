package ru.practicum.shareit.booking.model;

import lombok.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "bookings", schema = "public")
@Getter
@Setter
@ToString
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "start_date")
    private LocalDateTime start;
    @Column(name = "end_date")
    private LocalDateTime end;
    @OneToOne()
    @JoinColumn(name = "item_id", referencedColumnName = "id")
    private Item item;
    @OneToOne()
    @JoinColumn(name = "booker_id", referencedColumnName = "id")
    private User booker;
    @Enumerated
    private Status status;

    public Booking(Booking newBooking) {
        this.setId(newBooking.getId());
        this.setBooker(newBooking.getBooker());
        this.setStart(newBooking.getStart());
        this.setEnd(newBooking.getEnd());
        this.setStatus(newBooking.getStatus());
        this.setItem(newBooking.getItem());
    }
}
