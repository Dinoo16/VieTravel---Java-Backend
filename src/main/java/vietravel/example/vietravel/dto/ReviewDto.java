package vietravel.example.vietravel.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
@Data
public class ReviewDto {
    private Long id;

    @NotNull(message = "User ID is required")
    private Long userId;

    @NotNull(message = "Tour ID is required")
    private Long tourId;

    @NotNull(message = "Rating is required")
    @Min(value = 1, message = "Rating must be at least 0")
    @Max(value = 5, message = "Rating must be at most 5")
    private Integer rating;

    @Size(max = 1000, message = "Comment must be less than 1000 characters")
    private String comment;
}
