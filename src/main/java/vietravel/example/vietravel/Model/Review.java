package vietravel.example.vietravel.Model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 1 user -> many reviews
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // 1 tour -> many reviews
    @ManyToOne
    @JoinColumn(name = "tour_id", nullable = false)
    private Tour tour;

    private Integer rating;

    @Column(columnDefinition = "TEXT")
    private String comment;
}

