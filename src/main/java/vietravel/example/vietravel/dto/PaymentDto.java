package vietravel.example.vietravel.dto;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;
import vietravel.example.vietravel.Enum.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class PaymentDto {
    private Long id;

    @NotNull(message = "Booking ID is required")
    private Long bookingId;

    private Long userId;

    @DecimalMin(value = "0.0", inclusive = false, message = "Amount must be greater than 0")
    private BigDecimal amount;
    private String currency;

    private String providerTransactionId;

    @NotNull(message = "Payment status is required")
    private PaymentStatus status;

    private LocalDateTime createdAt; // Thêm
    private LocalDateTime updatedAt;
}

