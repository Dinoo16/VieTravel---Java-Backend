package vietravel.example.vietravel.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import vietravel.example.vietravel.Enum.BookingStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingSummaryDto {
    private Long id;
    private Integer numberOfPeople;
    private BookingStatus status;
}
