package vietravel.example.vietravel.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vietravel.example.vietravel.Enum.BookingStatus;
import vietravel.example.vietravel.Model.Booking;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    Optional<Booking> findByBookingIdAndUser_Email(Long bookingId, String email);

    List<Booking> findByUser_Email(String email);

    Optional<Booking> findByPaypalOrderId(String orderId);

    List<Booking> findByStatusAndDateBefore(BookingStatus status, LocalDate date);

    @Query("SELECT b.tourSchedule.tour.tourId AS tourId, COUNT(b) AS bookingCount " +
            "FROM Booking b " +
            "WHERE b.status = 'PAID' " + // chỉ lấy booking đã thanh toán
            "GROUP BY b.tourSchedule.tour.tourId " +
            "ORDER BY COUNT(b) DESC")
    List<Object[]> findTrendingTours();
}
