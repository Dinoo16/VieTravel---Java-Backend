package vietravel.example.vietravel.Model;

import jakarta.persistence.*;
import lombok.*;
import vietravel.example.vietravel.Enum.BookingStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookingId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "tour_schedule_id")
    private TourSchedule tourSchedule;

    @OneToOne(mappedBy = "booking", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Payment payment;


    // Contact information
    private String contactName;
    private String contactEmail;
    private String contactPhone;

    // Ngày khởi hành
    private LocalDate date;

    @Enumerated(EnumType.STRING)
    private BookingStatus status;

    private int numberOfPeople;

    private String currency;

    private BigDecimal totalAmount;

    private String message;

    private String paypalOrderId;  // mapping với order PayPal
    private String paypalCaptureId; // mapping với capture

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

