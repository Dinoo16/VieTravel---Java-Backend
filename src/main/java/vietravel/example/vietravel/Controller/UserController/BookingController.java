package vietravel.example.vietravel.Controller.UserController;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import vietravel.example.vietravel.Service.ServiceInterface.BookingService;
import vietravel.example.vietravel.dto.BookingDto;
import vietravel.example.vietravel.dto.TourDto;

import java.util.List;
import java.util.Map;

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
        return ResponseEntity.status(HttpStatus.CREATED).body(createdBooking);
    }
    @PostMapping("/{id}/pending")
    public void savePendingOrder(@PathVariable Long id, @RequestParam String orderId) {
        bookingService.savePendingOrder(id, orderId);
    }
    @PostMapping("/paid")
    public void markPaid(@RequestBody Map<String, String> payload) {
        String orderId = payload.get("orderId");
        String captureId = payload.get("captureId");
        bookingService.markPaid(orderId, captureId);
    }


    @PostMapping("/failed")
    public void markFailed(@RequestParam String orderId) {
        bookingService.markFailed(orderId);
    }


    // Update booking (contact fields)
    @PutMapping("/{id}")
    public ResponseEntity<BookingDto> updateBooking(@PathVariable Long id, @RequestBody BookingDto bookingDto) {
        BookingDto updateBooking = bookingService.updateBookingContact(id, bookingDto);
        return ResponseEntity.ok(updateBooking);
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
