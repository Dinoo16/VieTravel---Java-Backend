package vietravel.example.vietravel.Controller.UserController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import vietravel.example.vietravel.Service.BookingService;
import vietravel.example.vietravel.dto.BookingDto;
import vietravel.example.vietravel.dto.TourDto;

import java.util.List;

@RestController
@RequestMapping("/api/user/bookings")
@RequiredArgsConstructor
@PreAuthorize("hasRole('USER')")
public class BookingController {

    private final BookingService bookingService;

    // Create booking
    @PostMapping
    public ResponseEntity<BookingDto> createBooking(@RequestBody BookingDto bookingDto) {
        BookingDto createdBooking = bookingService.createBooking(bookingDto);
        System.out.println(">>> booking request: " + bookingDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdBooking);
    }

    // Cancel booking
    @PutMapping("/{id}/cancel")
    public ResponseEntity<BookingDto> cancelBooking(@PathVariable Long id) {
        BookingDto cancelled = bookingService.cancelBooking(id);
        return ResponseEntity.ok(cancelled);
    }


    // User get booking by id
    @GetMapping("/{id}")
    public ResponseEntity<BookingDto> getBookingById(@PathVariable Long id) {
        return ResponseEntity.ok(bookingService.getBookingById(id));
    }

    // User get all bookings
    @GetMapping
    public ResponseEntity<List<BookingDto>> userGetAllBookings() {
        return ResponseEntity.ok(bookingService.userGetAllBookings());
    }

    // User get tours by bookings
    @GetMapping("/my-tours")
    public ResponseEntity<List<TourDto>> getMyTours() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(bookingService.getToursByUserBookings(email));
    }


}
