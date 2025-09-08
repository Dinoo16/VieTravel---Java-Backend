package vietravel.example.vietravel.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import vietravel.example.vietravel.Model.AvailableDate;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
@Data
public class TourDto {
    private Long id;

    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Destination is required")
    private String destinationName;

    @NotBlank(message = "Departure location is required")
    private String departure;

    @NotEmpty(message = "Category is required")
    List<String> categoryNames;

    private String guideName;

    @NotBlank(message = "Duration is required")
    private String duration;

    @NotNull(message = "Price is required")
    @Min(value = 1, message = "Price must be greater than zero")
    private Double price;

    @NotBlank(message = "Description is required")
    private String description;

    private String backgroundImage;

    private List<String> gallery;

    private List<TourPlanDto> tourPlans;

    private List<ReviewDto> reviews;

    private List<AvailableDate> availableDates;

    // new attribute availableTimes
    private List<LocalTime> availableTimes;
}