package vietravel.example.vietravel.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
@Data
public class TourPlanDto {

    private Long id;

    private Long tourId;

    @NotNull(message = "Day is required")
    @Min(value = 1, message = "Day must be greater than or equal to 1")
    private Integer day;

    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Content is required")
    private String content;
}
