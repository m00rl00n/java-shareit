package ru.practicum.shareit.booking.repository;


import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Integer> {
    List<Booking> findBookingsByStatusAndStartIsAfterOrderByStartAsc(Status status, LocalDateTime ldt);

    List<Booking> findBookingsByStatusAndEndIsBeforeOrderByStartDesc(Status status, LocalDateTime ldt);

    List<Booking> findBookingsByBookerIdAndItemId(Integer id, Integer itemId);

    Booking findFirstByItemIdAndStartIsBeforeAndStatusIsOrderByStartDesc(Integer id, LocalDateTime ldt, Status status);

    Booking findTopByItemIdAndStartIsAfterAndStatusIsOrderByStartAsc(Integer itemId, LocalDateTime ldt, Status status);

    List<Booking> findAllByItemOwnerAndEndBefore(User user, LocalDateTime end, Sort sort);

    List<Booking> findAllByItemOwnerAndStartAfter(User user, LocalDateTime start, Sort sort);

    List<Booking> findAllByItemOwnerAndStatusEquals(User user, Status status, Sort sort);

    List<Booking> findAllByBooker(User user, Sort sort);

    List<Booking> findAllByBookerAndStartBeforeAndEndAfter(User user, LocalDateTime start, LocalDateTime end,
                                                           Sort sort);

    List<Booking> findAllByBookerAndEndBefore(User booker, LocalDateTime ltd, Sort sort);

    List<Booking> findAllByBookerAndStartAfter(User user, LocalDateTime ltd, Sort sort);

    List<Booking> findAllByBookerAndStatusEquals(User user, Status status, Sort sort);

    List<Booking> findAllByItemOwner(User user, Sort sort);

    List<Booking> findAllByItemOwnerAndStartBeforeAndEndAfter(User user, LocalDateTime start, LocalDateTime end,
                                                              Sort sort);

}


