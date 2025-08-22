package vietravel.example.vietravel.Service.Implement;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import vietravel.example.vietravel.Enum.BookingStatus;
import vietravel.example.vietravel.Model.*;
import vietravel.example.vietravel.Repository.BookingRepository;
import vietravel.example.vietravel.Repository.TourRepository;
import vietravel.example.vietravel.Repository.TourScheduleRepository;
import vietravel.example.vietravel.Repository.UserRepository;
import vietravel.example.vietravel.Service.ServiceInterface.BookingService;
import vietravel.example.vietravel.dto.BookingDto;
import vietravel.example.vietravel.dto.ReviewDto;
import vietravel.example.vietravel.dto.TourDto;
import vietravel.example.vietravel.dto.TourPlanDto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
        dto.setTourScheduleId(booking.getTourSchedule().getId());
        dto.setTourId(booking.getTourSchedule().getTour().getTourId());
        dto.setContactName(booking.getContactName());
        dto.setContactEmail(booking.getContactEmail());
        dto.setContactPhone(booking.getContactPhone());
        dto.setDate(booking.getDate());
        dto.setStatus(booking.getStatus());
        dto.setNumberOfPeople(booking.getNumberOfPeople());
        dto.setTotalAmount(booking.getTotalAmount());
        dto.setMessage(booking.getMessage());
        return dto;
    }

    @Override
    public BookingDto createBooking(BookingDto bookingDto) {
        // 3. Lấy thông tin User từ SecurityContext
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        MyUserDetails userDetails = (MyUserDetails) authentication.getPrincipal();
        Long userId = userDetails.getId();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));


        // 1. Validate ngày khởi hành
        if (bookingDto.getDate().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Departure date must be today or in the future");
        }

        // 2. Validate số người
        if (bookingDto.getNumberOfPeople() <= 0) {
            throw new IllegalArgumentException("Number of people must be greater than 0");
        }


        // 4. Lấy Tour từ tourId
        Long tourId = bookingDto.getTourId(); // bạn cần thêm trường này vào BookingDto
        Tour tour = tourRepository.findById(tourId)
                .orElseThrow(() -> new RuntimeException("Tour not found with id: " + tourId));

        // 5. Tính ngày đi và ngày về
        LocalDateTime departureDateTime = bookingDto.getDate().atStartOfDay();
        LocalDateTime returnDateTime = departureDateTime.plusDays(tour.getDuration());

        // 6. Tính totalAmout
        BigDecimal totalAmount = BigDecimal.valueOf(tour.getPrice() * bookingDto.getNumberOfPeople());

        // 7. Tạo TourSchedule mới (tạm không có hướng dẫn viên)
        TourSchedule newSchedule = TourSchedule.builder()
                .tour(tour)
                .departureDate(departureDateTime)
                .returnTime(returnDateTime)
                .guides(List.of()) // có thể cập nhật sau
                .build();
        tourScheduleRepository.save(newSchedule);

        // 8. Tạo Booking mới gắn với schedule này
        Booking booking = Booking.builder()
                .user(user)
                .tourSchedule(newSchedule)
                .contactName(bookingDto.getContactName())
                .contactEmail(bookingDto.getContactEmail())
                .contactPhone(bookingDto.getContactPhone())
                .date(bookingDto.getDate())
                .status(BookingStatus.PENDING)
                .numberOfPeople(bookingDto.getNumberOfPeople())
                .currency(bookingDto.getCurrency())
                .totalAmount(totalAmount)
                .message(bookingDto.getMessage())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        Booking saved = bookingRepository.save(booking);

        // 9. Trả về DTO
        return toDto(saved);
    }

    // Lưu orderId khi tạo PayPal order
    @Override
    public void savePendingOrder(Long bookingId, String orderId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));
        booking.setPaypalOrderId(orderId);
        booking.setUpdatedAt(LocalDateTime.now());
        bookingRepository.save(booking);
    }

    // Cập nhật PAID khi capture thành công
    @Override
    public void markPaid(String orderId, String captureId) {
        Booking booking = bookingRepository.findByPaypalOrderId(orderId)
                .orElseThrow(() -> new RuntimeException("Booking not found by orderId"));
        booking.setStatus(BookingStatus.PAID);
        booking.setPaypalCaptureId(captureId);
        booking.setUpdatedAt(LocalDateTime.now());
        bookingRepository.save(booking);
    }

    // Cập nhật FAILED
    @Override
    public void markFailed(String orderId) {
        Booking booking = bookingRepository.findByPaypalOrderId(orderId)
                .orElseThrow(() -> new RuntimeException("Booking not found by orderId"));
        booking.setStatus(BookingStatus.FAILED);
        booking.setUpdatedAt(LocalDateTime.now());
        bookingRepository.save(booking);
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


    // Get booking by id
    @Override
    public BookingDto getBookingById(Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String currentEmail = auth.getName();
        boolean isAdmin = auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        Booking booking;

        if (isAdmin) {
            booking = bookingRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Booking not found"));
        } else {
            booking = bookingRepository.findByBookingIdAndUser_Email(id, currentEmail)
                    .orElseThrow(() -> new AccessDeniedException("You are not allowed to access this booking"));
        }

        return toDto(booking);
    }

    // User get bookings
    @Override
    public List<BookingDto> userGetAllBookings() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String currentEmail = auth.getName();
        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        List<Booking> bookings;

        if (isAdmin) {
            bookings = bookingRepository.findAll();
        } else {
            bookings = bookingRepository.findByUser_Email(currentEmail);
        }

        return bookings.stream().map(this::toDto).toList();
    }


    @Override
    public List<BookingDto> getAllBookings() {
        return bookingRepository.findAll()
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<TourDto> getToursByUserBookings(String userEmail) {
        List<Booking> bookings = bookingRepository.findByUser_Email(userEmail);

        return bookings.stream()
                .map(b -> b.getTourSchedule().getTour())
                .distinct()
                .map(this::toTourDto) // hoặc this::toTourDto nếu bạn viết convert trong service
                .toList();
    }

    private TourDto toTourDto(Tour tour) {
        TourDto dto = new TourDto();
        dto.setId(tour.getTourId());
        dto.setTitle(tour.getTitle());
        dto.setDestinationName(tour.getDestination().getName());
        dto.setDeparture(tour.getDeparture());
        dto.setDepartureTime(tour.getDepartureTime());
        dto.setReturnTime(tour.getReturnTime());
        dto.setCategoryNames(tour.getCategories().stream().map(Category::getName).collect(Collectors.toList()));
        dto.setGuideName(tour.getGuide() != null ? tour.getGuide().getName() : null);
        dto.setDuration(tour.getDuration() + (tour.getDuration() == 1 ? " day" : " days"));
        dto.setPrice(tour.getPrice());
        dto.setDescription(tour.getDescription());
        String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().toUriString();
        dto.setBackgroundImage(baseUrl + tour.getBackgroundImage());
        dto.setGallery(tour.getGallery());
        dto.setTourPlans(
                tour.getTourPlans() != null ?
                        tour.getTourPlans().stream().map(plan -> {
                            TourPlanDto planDto = new TourPlanDto();
                            planDto.setId(plan.getId());
                            planDto.setTourId(plan.getTour().getTourId());
                            planDto.setDay(plan.getDay());
                            planDto.setTitle(plan.getTitle());
                            planDto.setContent(plan.getContent());
                            return planDto;
                        }).collect(Collectors.toList())
                        : new ArrayList<>()
        );
        if (tour.getReviews() != null) {
            dto.setReviews(
                    tour.getReviews().stream()
                            .map(review -> {
                                ReviewDto reviewDto = new ReviewDto();
                                reviewDto.setId(review.getId());
                                reviewDto.setUserId(review.getUser().getUserId());
                                reviewDto.setTourId(review.getTour().getTourId());
                                reviewDto.setRating(review.getRating());
                                reviewDto.setComment(review.getComment());
                                return reviewDto;
                            })
                            .collect(Collectors.toList())
            );
        } else {
            dto.setReviews(new ArrayList<>());
        }

        return dto;
    }
}
