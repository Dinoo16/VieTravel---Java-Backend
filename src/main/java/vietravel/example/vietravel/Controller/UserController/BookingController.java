package vietravel.example.vietravel.Controller.UserController;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vietravel.example.vietravel.Service.BookingService;
import vietravel.example.vietravel.dto.BookingDto;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
@PreAuthorize("hasRole('USER') or hasRole('GUIDE')")
public class BookingController {

    private final BookingService bookingService;

    // Create booking
    @PostMapping
    public ResponseEntity<BookingDto> createBooking(@Valid @RequestBody BookingDto bookingDto) {
        return ResponseEntity.ok(bookingService.createBooking(bookingDto));
    }

    @GetMapping("/schedule/{tourScheduleId}")
    public ResponseEntity<List<BookingDto>> getBookingsByTourScheduleId(@PathVariable Long tourScheduleId) {
        return ResponseEntity.ok(bookingService.getBookingsByTourScheduleId(tourScheduleId));
    }



}
