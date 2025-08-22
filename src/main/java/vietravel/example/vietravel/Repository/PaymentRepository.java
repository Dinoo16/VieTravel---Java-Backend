package vietravel.example.vietravel.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vietravel.example.vietravel.Model.Booking;
import vietravel.example.vietravel.Model.Payment;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    // Find payment by booking id
    List<Payment> findByBookingBookingId(Long bookingId);

    Optional<Payment> findByPaymentIdAndUser_Email(Long paymentId, String email);

    Optional<Payment> findByProviderTransactionId(String providerTransactionId);
}
