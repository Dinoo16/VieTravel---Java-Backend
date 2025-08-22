package vietravel.example.vietravel.dto;
import jakarta.validation.constraints.*;
import lombok.Data;
import vietravel.example.vietravel.Enum.BookingStatus;
import vietravel.example.vietravel.Model.TourSchedule;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class BookingDto {
    private Long id;

    private Long tourId;

    private Long userId;

    @NotNull(message = "Booking date is required")
    private LocalDate date;

    @NotNull(message = "Tour schedule ID is required")
    private Long tourScheduleId;

    @NotBlank(message = "Contact name is required")
    private String contactName;

    @NotBlank(message = "Contact email is required")
    @Email(message = "Invalid email format")
    private String contactEmail;

    @NotBlank(message = "Contact phone is required")
    @Pattern(
            regexp = "^(\\+\\d{1,3}[- ]?)?\\d{9,15}$",
            message = "Invalid phone number"
    )
    private String contactPhone;

    private BookingStatus status;

    @NotNull(message = "Number of people is required")
    @Min(value = 1, message = "At least one person is required")
    private Integer numberOfPeople;

    private String currency;


    @Null(message = "Total amount is calculated automatically")
    private BigDecimal totalAmount;


    private String message;

    private String paypalOrderId;  // mapping với order PayPal
    private String paypalCaptureId;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
