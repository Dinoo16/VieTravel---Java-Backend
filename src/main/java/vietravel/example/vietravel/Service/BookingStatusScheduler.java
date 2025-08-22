package vietravel.example.vietravel.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import vietravel.example.vietravel.Enum.BookingStatus;
import vietravel.example.vietravel.Model.Booking;
import vietravel.example.vietravel.Repository.BookingRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingStatusScheduler {

    private final BookingRepository bookingRepository;

    /**
     * It runs at 0h every day to update booking status
     */
    @Scheduled(cron = "0 0 0 * * ?") // 0h00 every day
    public void updateExpiredBookings() {
        LocalDate today = LocalDate.now();

        //Get all booking status PENDING that departure  < today
        List<Booking> expiredBookings = bookingRepository
                .findByStatusAndDateBefore(BookingStatus.PENDING, today);

        for (Booking booking : expiredBookings) {
            booking.setStatus(BookingStatus.EXPIRED);
            booking.setUpdatedAt(LocalDateTime.now());
        }

        if (!expiredBookings.isEmpty()) {
            bookingRepository.saveAll(expiredBookings);
            System.out.println("Updated " + expiredBookings.size() + " expired bookings at " + LocalDateTime.now());
        }
    }
}
