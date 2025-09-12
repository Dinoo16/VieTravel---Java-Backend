package vietravel.example.vietravel.Model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import vietravel.example.vietravel.Enum.BookingStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookingId;

    @Column(name = "tour_id", nullable = false)
    private Long tourId;

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

    // Departure Date
    private LocalDate date;

    // Departure Time
    private LocalTime time;

    @Enumerated(EnumType.STRING)
    private BookingStatus status;

    private int numberOfPeople;

    private String currency;

    private BigDecimal totalAmount;

    private String message;

    private String paypalOrderId;  // mapping với order PayPal
    private String paypalCaptureId; // mapping với capture

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}

