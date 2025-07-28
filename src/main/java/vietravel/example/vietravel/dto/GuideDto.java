package vietravel.example.vietravel.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import vietravel.example.vietravel.Enum.ExperienceLevel;
import vietravel.example.vietravel.Enum.JobStatus;

import java.util.List;

@Data
public class GuideDto {
    private Long id;

    @NotNull(message = "User ID is required")
    private Long userId;

    @NotBlank(message = "Name is required")
    @Size(max = 100, message = "Name must be less than 100 characters")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    private String email;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^\\+?[0-9]{7,15}$", message = "Phone number must be valid")
    private String phoneNo;

    @Size(max = 1000, message = "Bio must be less than 1000 characters")
    private String bio;

    private String avatar;

    private String role;

    @NotNull(message = "Experience level is required")
    private String experienceLevel;

    @Min(value = 0, message = "Job achievement must be zero or positive")
    private Integer jobAchievement;

    @NotNull(message = "Job status is required")
    private String jobStatus;

    @Min(value = 0, message = "Job achievement must be zero or positive")
    private Integer workExperience;

    private List<Long> tourScheduleIds;
}
