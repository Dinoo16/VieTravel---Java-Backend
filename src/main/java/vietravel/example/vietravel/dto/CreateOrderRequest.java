package vietravel.example.vietravel.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record CreateOrderRequest(
        @NotNull Long bookingId,
        @NotBlank String currency,
        @DecimalMin("0.01") BigDecimal amount,
        @NotBlank String description
) {}
