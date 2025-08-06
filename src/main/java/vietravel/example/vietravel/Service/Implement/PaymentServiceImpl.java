package vietravel.example.vietravel.Service.Implement;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vietravel.example.vietravel.Enum.BookingStatus;
import vietravel.example.vietravel.Enum.PaymentStatus;
import vietravel.example.vietravel.Model.Booking;
import vietravel.example.vietravel.Model.Payment;
import vietravel.example.vietravel.Repository.BookingRepository;
import vietravel.example.vietravel.Repository.PaymentRepository;
import vietravel.example.vietravel.Service.PaymentService;
import vietravel.example.vietravel.dto.PaymentDto;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final BookingRepository bookingRepository;

    @Override
    @Transactional
    public PaymentDto createPayment(PaymentDto dto) {
        Booking booking = bookingRepository.findById(dto.getBookingId())
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        // Check the status of booking
        if (booking.getStatus() != BookingStatus.PENDING) {
            throw new IllegalStateException("Only PENDING bookings can be paid");
        }

        Payment payment = Payment.builder()
                .booking(booking)
                .amount(booking.getTotalAmount())
                .paymentMethod(dto.getPaymentMethod())
                .status(PaymentStatus.PENDING)
                .build();

        Payment saved = paymentRepository.save(payment);

        // Đồng bộ trạng thái Booking
        booking.setStatus(BookingStatus.PENDING);
        bookingRepository.save(booking);

        return toDto(saved);
    }

    @Override
    @Transactional
    public PaymentDto updatePaymentStatus(Long paymentId, PaymentStatus status) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));

        payment.setStatus(status);
        paymentRepository.save(payment);

        // Đồng bộ trạng thái booking theo status mới
        Booking booking = payment.getBooking();
        switch (status) {
            case PAID -> booking.setStatus(BookingStatus.PAID);
            case FAILED -> booking.setStatus(BookingStatus.PENDING); // hoặc FAILED nếu có trạng thái đó
            case CANCELLED, REFUNDED -> booking.setStatus(BookingStatus.CANCELLED);
            default -> {}
        }
        bookingRepository.save(booking);

        return toDto(payment);
    }

    @Override
    public PaymentDto getPaymentById(Long id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payment not found"));
        return toDto(payment);
    }

    private PaymentDto toDto(Payment payment) {
        return PaymentDto.builder()
                .id(payment.getPaymentId())
                .bookingId(payment.getBooking().getBookingId())
                .amount(payment.getAmount())
                .paymentMethod(payment.getPaymentMethod())
                .status(payment.getStatus())
                .build();
    }
}
