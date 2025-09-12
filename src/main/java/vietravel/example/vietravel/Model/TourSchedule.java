package vietravel.example.vietravel.Model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TourSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Lịch này thuộc tour nào
    @ManyToOne
    @JoinColumn(name = "tour_id", nullable = false)
    private Tour tour;

    // Ngày khởi hành
    private LocalDate departureDate;

    // Giờ khởi hành
    private LocalTime departureTime;

    private LocalDate returnTime;


    // Gán nhiều hướng dẫn viên cho tour này
    @ManyToMany
    @JoinTable(
            name = "tour_schedule_guides",
            joinColumns = @JoinColumn(name = "tour_schedule_id"),
            inverseJoinColumns = @JoinColumn(name = "guide_id")
    )
    private List<Guide> guides;

    @OneToMany(mappedBy = "tourSchedule", cascade = CascadeType.ALL)
    private List<Booking> bookings = new ArrayList<>();

}
