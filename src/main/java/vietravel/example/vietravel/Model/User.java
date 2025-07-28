package vietravel.example.vietravel.Model;

import jakarta.persistence.*;
import lombok.*;
import vietravel.example.vietravel.Enum.UserRole;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    private String name;

    @Column(unique = true, nullable = false)
    private String email;

    private String password;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    private String phone;

    @Column(columnDefinition = "TEXT")
    private String avatar;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Booking> bookings;
}

