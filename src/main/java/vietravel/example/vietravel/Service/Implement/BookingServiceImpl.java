package vietravel.example.vietravel.Service.Implement;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vietravel.example.vietravel.Enum.BookingStatus;
import vietravel.example.vietravel.Model.Booking;
import vietravel.example.vietravel.Model.Tour;
import vietravel.example.vietravel.Model.TourSchedule;
import vietravel.example.vietravel.Model.User;
import vietravel.example.vietravel.Repository.BookingRepository;
import vietravel.example.vietravel.Repository.TourRepository;
import vietravel.example.vietravel.Repository.TourScheduleRepository;
import vietravel.example.vietravel.Repository.UserRepository;
import vietravel.example.vietravel.Service.BookingService;
import vietravel.example.vietravel.dto.BookingDto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final TourRepository tourRepository;
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final TourScheduleRepository tourScheduleRepository;

    private BookingDto toDto(Booking booking) {
        BookingDto dto = new BookingDto();
        dto.setId(booking.getBookingId());
        dto.setUserId(booking.getUser().getUserId());
        dto.setTourId(booking.getTourSchedule().getTour().getTourId());
        dto.setDate(booking.getDate());
        dto.setStatus(booking.getStatus());
        dto.setNumberOfPeople(booking.getNumberOfPeople());
        dto.setTotalAmount(booking.getTotalAmount());
        return dto;
    }

    @Override
    public BookingDto createBooking(BookingDto bookingDto) {

        // 1. Validate ngày khởi hành
        if (bookingDto.getDate().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Departure date must be today or in the future");
        }

        // 2. Validate số người
        if (bookingDto.getNumberOfPeople() <= 0) {
            throw new IllegalArgumentException("Number of people must be greater than 0");
        }

        // 3. Lấy thông tin User
        User user = userRepository.findById(bookingDto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + bookingDto.getUserId()));

        // 4. Lấy Tour từ tourId
        Long tourId = bookingDto.getTourId(); // bạn cần thêm trường này vào BookingDto
        Tour tour = tourRepository.findById(tourId)
                .orElseThrow(() -> new RuntimeException("Tour not found with id: " + tourId));

        // 5. Tính ngày đi và ngày về
        LocalDateTime departureDateTime = bookingDto.getDate().atStartOfDay();
        LocalDateTime returnDateTime = departureDateTime.plusDays(tour.getDuration());

        // 6. Tạo TourSchedule mới (tạm không có hướng dẫn viên)
        TourSchedule newSchedule = TourSchedule.builder()
                .tour(tour)
                .departureDate(departureDateTime)
                .returnTime(returnDateTime)
                .guides(List.of()) // có thể cập nhật sau
                .build();
        tourScheduleRepository.save(newSchedule);

        // 7. Tạo Booking mới gắn với schedule này
        Booking booking = Booking.builder()
                .user(user)
                .tourSchedule(newSchedule)
                .date(bookingDto.getDate())
                .status(BookingStatus.PENDING)
                .numberOfPeople(bookingDto.getNumberOfPeople())
                .totalAmount(bookingDto.getTotalAmount())
                .build();

        Booking saved = bookingRepository.save(booking);

        // 8. Trả về DTO
        return toDto(saved);
    }

    @Override
    public BookingDto cancelBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        if (booking.getStatus() == BookingStatus.CANCELLED) {
            throw new IllegalStateException("Booking is already cancelled");
        }

        booking.setStatus(BookingStatus.CANCELLED);
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
    public List<BookingDto> getAllBookings() {
        return bookingRepository.findAll()
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}
