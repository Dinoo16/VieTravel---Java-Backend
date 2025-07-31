package vietravel.example.vietravel.Service;

import vietravel.example.vietravel.dto.BookingDto;

import java.util.List;

public interface BookingService {

    BookingDto createBooking(BookingDto bookingDto);
//    BookingDto updateBooking(Long id, BookingDto bookingDto);
    void deleteBooking(Long id);
    BookingDto getBookingById(Long id);
    List<BookingDto> getBookingsByUserId(Long userId);
    List<BookingDto> getBookingsByTourScheduleId(Long tourScheduleId);
    List<BookingDto> getAllBookings();

}
