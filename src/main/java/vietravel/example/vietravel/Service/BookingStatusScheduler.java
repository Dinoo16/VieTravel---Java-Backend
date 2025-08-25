package vietravel.example.vietravel.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vietravel.example.vietravel.Enum.BookingStatus;
import vietravel.example.vietravel.Model.Booking;
import vietravel.example.vietravel.Repository.BookingRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingStatusScheduler {

    private final BookingRepository bookingRepository;

    /**
     * It runs at 0h every day to update booking status
     */
    @Transactional
    @Scheduled(cron = "0 * * * * ?")
    public void updateExpiredBookings() {
        log.info("=== SCHEDULER STARTED ===");
        LocalDate today = LocalDate.now();
        log.info("Today's date: {}", today);

        //Get all booking status PENDING that departure  < today
        List<Booking> expiredBookings = bookingRepository
                .findByStatusAndDateBefore(BookingStatus.PENDING, today);
        log.info("Found {} expired bookings", expiredBookings.size());

        for (Booking booking : expiredBookings) {
            log.info("Updating booking ID: {}, Date: {}, Status: {} -> {}",
                    booking.getBookingId(),
                    booking.getDate(),
                    booking.getStatus(),
                    BookingStatus.EXPIRED);
                    booking.setStatus(BookingStatus.EXPIRED);
            booking.setUpdatedAt(LocalDateTime.now());
        }

        if (!expiredBookings.isEmpty()) {
            bookingRepository.saveAll(expiredBookings);
            log.info("Successfully updated {} bookings", expiredBookings.size());
            System.out.println("Updated " + expiredBookings.size() + " expired bookings at " + LocalDateTime.now());
        } else {
        log.info("No expired bookings found");
    }
    }
}
