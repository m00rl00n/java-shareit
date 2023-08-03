package ru.practicum.shareit.booking.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "bookings")
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode(of = "id")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    @Column(name = "start_time")
    LocalDateTime start;
    @Column(name = "end_time")
    LocalDateTime end;
    @ManyToOne
    @JoinColumn(name = "item_id", referencedColumnName = "id")
    Item item;
    @ManyToOne
    @JoinColumn(name = "booker_id", referencedColumnName = "id")
    User booker;
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    Status status;
}
