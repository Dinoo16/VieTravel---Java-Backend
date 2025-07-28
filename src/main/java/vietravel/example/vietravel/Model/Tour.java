package vietravel.example.vietravel.Model;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data// getter, setter
@NoArgsConstructor
@AllArgsConstructor
@Builder //	Implements the builder pattern
public class Tour {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tourId;

    private String title;

    // Destination (many tours -> one destination)
    @ManyToOne
    @JoinColumn(name = "destination_id")
    private Destination destination;

    private String departure;

    private LocalDateTime departureTime;

    private LocalDateTime returnTime;

    // Category (many tours -> one category)
    @ManyToMany
    @JoinTable(
            name = "tour_categories",
            joinColumns = @JoinColumn(name = "tour_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private List<Category> categories;

    // Guide (many tours -> one guide)
    @ManyToOne
    @JoinColumn(name = "guide_id")
    private Guide guide;

    // Duration and price
    private Integer duration;

    private Double price;

    @Column(columnDefinition = "TEXT")
    private String description;

    // Fix later
    @Column(columnDefinition = "TEXT")
    private String backgroundImage;

    // Gallery: list of image URLs
    @ElementCollection
    @CollectionTable(name = "tour_gallery", joinColumns = @JoinColumn(name = "tour_id"))
    @Column(name = "image_url")
    private List<String> gallery;

    @OneToMany(mappedBy = "tour", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TourPlan> tourPlans;

    // Available dates
    @ElementCollection
    @CollectionTable(name = "tour_available_dates", joinColumns = @JoinColumn(name = "tour_id"))
    @Column(name = "available_date")
    private List<LocalDateTime> availableDates;

    // Relationships
    @OneToMany(mappedBy = "tour", cascade = CascadeType.ALL)
    private List<Booking> bookings;

    @OneToMany(mappedBy = "tour", cascade = CascadeType.ALL)
    private List<Review> reviews;

    @OneToMany(mappedBy = "tour", cascade = CascadeType.ALL)
    private List<TourSchedule> tourSchedules;
}
