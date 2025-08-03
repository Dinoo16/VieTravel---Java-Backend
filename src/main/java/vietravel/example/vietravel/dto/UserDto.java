package vietravel.example.vietravel.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import vietravel.example.vietravel.Enum.UserRole;

import java.util.Date;

@Data
public class UserDto {
    private Long id;

    private String name;

    @Email(message = "Email is not valid")
    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;

    private UserRole role;

    @Pattern(regexp = "\\d{10}", message = "Phone must be 10 digits")
    private String phone;

    private String avatar;

    private String address;

    private Date dateOfBirth;

    private String bio;

}
