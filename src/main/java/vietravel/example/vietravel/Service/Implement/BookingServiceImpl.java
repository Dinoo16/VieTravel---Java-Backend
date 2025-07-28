package vietravel.example.vietravel.Service.Implement;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vietravel.example.vietravel.Model.Booking;
import vietravel.example.vietravel.Model.TourSchedule;
import vietravel.example.vietravel.Model.User;
import vietravel.example.vietravel.Repository.BookingRepository;
import vietravel.example.vietravel.Repository.TourScheduleRepository;
import vietravel.example.vietravel.Repository.UserRepository;
import vietravel.example.vietravel.Service.BookingService;
import vietravel.example.vietravel.dto.BookingDto;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final TourScheduleRepository tourScheduleRepository;

    private BookingDto toDto(Booking booking) {
        BookingDto dto = new BookingDto();
        dto.setId(booking.getBookingId());
        dto.setUserId(booking.getUser().getUserId());
        dto.setTourScheduleId(booking.getTourSchedule().getId());
        dto.setDate(booking.getDate());
        dto.setStatus(booking.getStatus());
        dto.setNumberOfPeople(booking.getNumberOfPeople());
        dto.setTotalAmount(booking.getTotalAmount());
        return dto;
    }

    private Booking toEntity(BookingDto dto) {
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        TourSchedule schedule = tourScheduleRepository.findById(dto.getTourScheduleId())
                .orElseThrow(() -> new RuntimeException("Tour schedule not found"));

        return Booking.builder()
                .user(user)
                .tourSchedule(schedule)
                .date(dto.getDate())
                .status(dto.getStatus())
                .numberOfPeople(dto.getNumberOfPeople())
                .totalAmount(dto.getTotalAmount())
                .build();
    }

    @Override
    public BookingDto createBooking(BookingDto bookingDto) {
        Booking booking = toEntity(bookingDto);
        Booking saved = bookingRepository.save(booking);
        return toDto(saved);
    }

    @Override
    public BookingDto updateBooking(Long id, BookingDto bookingDto) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        booking.setDate(bookingDto.getDate());
        booking.setStatus(bookingDto.getStatus());
        booking.setNumberOfPeople(bookingDto.getNumberOfPeople());
        booking.setTotalAmount(bookingDto.getTotalAmount());

        // Optionally update user or tour schedule:
        if (!booking.getUser().getUserId().equals(bookingDto.getUserId())) {
            User user = userRepository.findById(bookingDto.getUserId())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            booking.setUser(user);
        }

        if (!booking.getTourSchedule().getId().equals(bookingDto.getTourScheduleId())) {
            TourSchedule schedule = tourScheduleRepository.findById(bookingDto.getTourScheduleId())
                    .orElseThrow(() -> new RuntimeException("Tour schedule not found"));
            booking.setTourSchedule(schedule);
        }

        return toDto(bookingRepository.save(booking));
    }

    @Override
    public void deleteBooking(Long id) {
        if (!bookingRepository.existsById(id)) {
            throw new RuntimeException("Booking not found");
        }
        bookingRepository.deleteById(id);
    }

    @Override
    public BookingDto getBookingById(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found"));
        return toDto(booking);
    }

    @Override
    public List<BookingDto> getBookingsByUserId(Long userId) {
        return bookingRepository.findByUserUserId(userId)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<BookingDto> getBookingsByTourScheduleId(Long tourScheduleId) {
        return bookingRepository.findByTourScheduleId(tourScheduleId)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<BookingDto> getAllBookings() {
        return bookingRepository.findAll()
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}
