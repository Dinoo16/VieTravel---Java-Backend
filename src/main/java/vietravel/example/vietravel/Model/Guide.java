package vietravel.example.vietravel.Model;

import jakarta.persistence.*;
import lombok.*;
import vietravel.example.vietravel.Enum.ExperienceLevel;
import vietravel.example.vietravel.Enum.JobStatus;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Guide {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 1 guide -> 1 user
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    private String name;

    private String email;

    @Column(name = "phone_no")
    private String phoneNo;

    private String bio;

    private String avatar;

    // This could be something like "Northern Vietnam Tour Guide"
    private String role;

    private ExperienceLevel experienceLevel;

    @Column(name = "job_achievement")
    private Integer jobAchievement; // number of completed tours

    @Column(name = "job_status")
    private JobStatus jobStatus; // true = available, false = unavailable

    @Column(name = "work_experience")
    private Integer workExperience; // in years

    // Relationships
    @OneToMany(mappedBy = "guide", cascade = CascadeType.ALL)
    private List<Tour> tours;

    @ManyToMany(mappedBy = "guides")
    private List<TourSchedule> tourSchedules;
}

