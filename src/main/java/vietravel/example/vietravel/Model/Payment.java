package vietravel.example.vietravel.Model;

import jakarta.persistence.*;
import lombok.*;
import vietravel.example.vietravel.Enum.PaymentStatus;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentId;

    @OneToOne
    @JoinColumn(name = "booking_id", referencedColumnName = "bookingId")
    private Booking booking;

    private double amount;

    private String paymentMethod; // e.g., "Credit Card", "Paypal", "ApplePay"

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;
}
