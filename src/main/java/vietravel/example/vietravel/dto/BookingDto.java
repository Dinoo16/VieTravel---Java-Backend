package vietravel.example.vietravel.dto;
import jakarta.validation.constraints.*;
import lombok.Data;
import vietravel.example.vietravel.Enum.BookingStatus;
import vietravel.example.vietravel.Model.TourSchedule;

import java.time.LocalDate;
@Data
public class BookingDto {
    private Long id;

    @NotNull(message = "User ID is required")
    private Long userId;

    private Long tourId;

    @NotNull(message = "Booking date is required")
    private LocalDate date;

    private BookingStatus status;

    @NotNull(message = "Number of people is required")
    @Min(value = 1, message = "At least one person is required")
    private Integer numberOfPeople;

    @NotNull(message = "Total amount is required")
    @Positive(message = "Total amount must be positive")
    private Double totalAmount;
}
