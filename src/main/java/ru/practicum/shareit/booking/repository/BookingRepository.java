package ru.practicum.shareit.booking.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Integer> {
    List<Booking> findBookingsByStatusAndStartIsAfterOrderByStartAsc(Status status, LocalDateTime ldt);

    List<Booking> findBookingsByStatusAndEndIsBeforeOrderByStartDesc(Status status, LocalDateTime ldt);

    Booking findFirstByItemIdAndStartIsBeforeAndStatusIsOrderByStartDesc(Integer itemId, LocalDateTime ldt, Status status);

    List<Booking> findBookingsByItemOwnerId(Integer ownerId);

    Booking findTopByItemIdAndStartIsAfterAndStatusIsOrderByStartAsc(Integer itemId, LocalDateTime ldt, Status status);

    List<Booking> findBookingsByBookerId(Integer bookerId);

    List<Booking> findBookingsByBookerIdAndItemId(Integer bookerId, Integer itemId);
}

