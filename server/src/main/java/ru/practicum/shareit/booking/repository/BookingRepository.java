package ru.practicum.shareit.booking.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    Page<Booking> findAllByItemOwnerAndEndBefore(User user, LocalDateTime end, Pageable pageable);

    Page<Booking> findAllByItemOwnerAndStartAfter(User user, LocalDateTime start, Pageable pageable);

    Page<Booking> findAllByItemOwnerAndStatusEquals(User user, Status status, Pageable pageable);

    Page<Booking> findAllByBooker(User user, Pageable pageable);

    Page<Booking> findAllByBookerAndStartBeforeAndEndAfter(User user, LocalDateTime start, LocalDateTime end,
                                                           Pageable pageable);

    Page<Booking> findAllByBookerAndEndBefore(User booker, LocalDateTime ltd, Pageable pageable);

    Page<Booking> findAllByBookerAndStartAfter(User user, LocalDateTime ltd, Pageable pageable);

    Page<Booking> findAllByBookerAndStatusEquals(User user, Status status, Pageable pageable);

    Page<Booking> findAllByItemOwner(User user, Pageable pageable);

    Page<Booking> findAllByItemOwnerAndStartBeforeAndEndAfter(User user, LocalDateTime start, LocalDateTime end,
                                                              Pageable pageable);

}


