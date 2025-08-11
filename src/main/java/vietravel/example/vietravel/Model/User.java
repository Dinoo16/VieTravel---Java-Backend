package vietravel.example.vietravel.Model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import vietravel.example.vietravel.Enum.UserRole;
import vietravel.example.vietravel.dto.UserDto;

import java.util.Date;
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

    private String address;

    private Date dateOfBirth;

    private String bio;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    private List<Booking> bookings;

    public User(UserDto userDto, PasswordEncoder passwordEncoder) {
        this.name = userDto.getName();
        this.email = userDto.getEmail();
        this.password = passwordEncoder.encode(userDto.getPassword());
        this.role = (userDto.getRole() != null) ? userDto.getRole() : UserRole.USER;
        this.phone = userDto.getPhone();
        this.avatar = userDto.getAvatar();
        this.address = userDto.getAddress();
        this.dateOfBirth = userDto.getDateOfBirth();
        this.bio = userDto.getBio();
    }

}

