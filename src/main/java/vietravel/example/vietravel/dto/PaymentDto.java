package vietravel.example.vietravel.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import vietravel.example.vietravel.Enum.PaymentStatus;

@Data
public class PaymentDto {
    private Long id;

    @NotNull(message = "Booking ID is required")
    private Long bookingId;

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Amount must be greater than 0")
    private Double amount;

    @NotNull(message = "Payment method is required")
    private String paymentMethod;

    @NotNull(message = "Payment status is required")
    private PaymentStatus status;
}

