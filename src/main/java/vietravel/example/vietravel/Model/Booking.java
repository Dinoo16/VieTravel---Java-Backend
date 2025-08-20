package vietravel.example.vietravel.Model;

import jakarta.persistence.*;
import lombok.*;
import vietravel.example.vietravel.Enum.BookingStatus;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookingId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "tour_schedule_id")
    private TourSchedule tourSchedule;

    // Contact information
    private String contactName;
    private String contactEmail;
    private String contactPhone;

    // Ngày khởi hành
    private LocalDate date;

    @Enumerated(EnumType.STRING)
    private BookingStatus status;

    private int numberOfPeople;

    private double totalAmount;

    private String message;
}

