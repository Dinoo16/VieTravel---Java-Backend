package vietravel.example.vietravel.dto;

import lombok.Data;
import vietravel.example.vietravel.Model.Booking;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Data
public class TourScheduleDto {
    private Long id;
    private Long tourId;
    private LocalDate departureDate;
    private LocalTime departureTime;
    private LocalDate returnTime;
    private List<Long> bookingIds;
    private List<Long> guideIds;
}
