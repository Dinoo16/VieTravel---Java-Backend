package vietravel.example.vietravel.Service.Implement;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vietravel.example.vietravel.Enum.BookingStatus;
import vietravel.example.vietravel.Enum.PaymentStatus;
import vietravel.example.vietravel.Model.Booking;
import vietravel.example.vietravel.Model.Payment;
import vietravel.example.vietravel.Model.Tour;
import vietravel.example.vietravel.Model.TourSchedule;
import vietravel.example.vietravel.Repository.BookingRepository;
import vietravel.example.vietravel.Repository.PaymentRepository;

import com.fasterxml.jackson.databind.JsonNode;
import vietravel.example.vietravel.Repository.TourRepository;
import vietravel.example.vietravel.Repository.TourScheduleRepository;
import vietravel.example.vietravel.Service.PayPalClient;
import vietravel.example.vietravel.Service.ServiceInterface.PaymentService;
import vietravel.example.vietravel.dto.PaymentDto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PayPalClient payPalClient;
    private final BookingRepository bookingRepository;
    private final PaymentRepository paymentRepository;
    private final TourRepository tourRepository;
    private final TourScheduleRepository tourScheduleRepository;

    private PaymentDto toDto(Payment payment) {
        PaymentDto dto = new PaymentDto();
        dto.setId(payment.getPaymentId());
        dto.setBookingId(payment.getBooking().getBookingId());
        dto.setUserId(payment.getUser().getUserId());
        dto.setAmount(payment.getAmount());
        dto.setCurrency("USD");
        dto.setProviderTransactionId(payment.getProviderTransactionId());
        dto.setStatus(payment.getStatus());
        dto.setCreatedAt(payment.getCreatedAt());
        dto.setUpdatedAt(payment.getUpdatedAt());
        return dto;
    }

    @Transactional
    @Override
    public String createPayment(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        JsonNode order = payPalClient.createOrder("USD", booking.getTotalAmount().toString(), "Tour booking " + bookingId,   "http://localhost:3000/payment-success/" + bookingId,
                "http://localhost:3000/payment-cancel/" + bookingId);

        String orderId = order.get("id").asText();

        Payment payment = Payment.builder()
                .booking(booking)
                .user(booking.getUser())
                .amount(booking.getTotalAmount())
                .currency("USD")
                .providerTransactionId(orderId)
                .status(PaymentStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        paymentRepository.save(payment);

        return order.get("links").get(1).get("href").asText(); // approve URL
    }

    @Transactional
    @Override
    public PaymentDto capturePayment(Long bookingId, String orderId) {
        JsonNode result = payPalClient.captureOrder(orderId);
        System.out.println("PayPal Capture Response: " + result.toPrettyString());
        // get captureID
        String captureId = result.has("id") ? result.get("id").asText() : null;

        // find payment by orderId
        Payment payment = paymentRepository.findByProviderTransactionId(orderId)
                .orElseThrow(() -> new RuntimeException("Payment not found with orderId: " + orderId));

        Booking booking = payment.getBooking();

        Tour tour = tourRepository.findById(booking.getTourId())
                .orElseThrow(() -> new RuntimeException("Tour not found with id: " + booking.getTourId()));

        LocalDate departureDate = booking.getDate();
        LocalTime departureTime = booking.getTime();
        LocalDate returnDateTime = departureDate.plusDays(tour.getDuration());

        // Check if tourSchedule already exists
        Optional<TourSchedule> existingSchedule = tourScheduleRepository.findByTourAndDepartureDateAndDepartureTime(
                tour, departureDate, departureTime);

        TourSchedule tourSchedule;
        if (existingSchedule.isPresent()) {
            // Use existing tourSchedule
            tourSchedule = existingSchedule.get();
            System.out.println("Using existing TourSchedule: " + tourSchedule.getId());
        } else {
            // Create new TourSchedule
            tourSchedule = TourSchedule.builder()
                    .tour(tour)
                    .departureDate(departureDate)
                    .departureTime(departureTime)
                    .returnTime(returnDateTime)
                    .guides(List.of())
                    .build();
            tourSchedule = tourScheduleRepository.save(tourSchedule);
            System.out.println("Created new TourSchedule: " + tourSchedule.getId());
        }
        // Update payment
        payment.setStatus(PaymentStatus.PAID);
        payment.setUpdatedAt(LocalDateTime.now());
        paymentRepository.save(payment);

        // Update booking
        booking.setStatus(BookingStatus.PAID);
        booking.setPaypalOrderId(orderId);
        booking.setPaypalCaptureId(captureId);
        booking.setTourSchedule(tourSchedule);
        bookingRepository.save(booking);

        return toDto(payment);
    }

    @Override
    public PaymentDto getPaymentById(Long paymentId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String currentEmail = auth.getName();
        boolean isAdmin = auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        Payment payment;

        if (isAdmin) {
            payment = paymentRepository.findById(paymentId)
                    .orElseThrow(() -> new RuntimeException("Payment not found"));
        } else {
            payment = paymentRepository.findByPaymentIdAndUser_Email(paymentId, currentEmail)
                    .orElseThrow(() -> new AccessDeniedException("You are not allowed to access this booking"));
        }
        return toDto(payment);
    }


    // Just admin get all Payments
    @Override
    public List<PaymentDto> getAllPayments() {
        return paymentRepository.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

}
