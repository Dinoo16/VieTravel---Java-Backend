package vietravel.example.vietravel.Service;

import vietravel.example.vietravel.dto.BookingDto;
import vietravel.example.vietravel.dto.TourDto;

import java.util.List;

public interface BookingService {

    BookingDto createBooking(BookingDto bookingDto);

    BookingDto cancelBooking(Long bookingId);
    void deleteBooking(Long id);
    BookingDto getBookingById(Long id);
    List<BookingDto> getAllBookings();

    List<BookingDto> userGetAllBookings();

    List<TourDto> getToursByUserBookings(String userEmail);

}
