package vietravel.example.vietravel.Model;
import jakarta.persistence.*;
import lombok.*;


@Entity
@Data
@Builder
public class TourPlan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int day;
    private String title;
    private String content;

    @ManyToOne
    @JoinColumn(name = "tour_id")
    private Tour tour;
}

