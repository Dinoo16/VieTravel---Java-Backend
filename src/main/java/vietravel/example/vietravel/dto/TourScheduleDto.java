package vietravel.example.vietravel.dto;

import lombok.Data;
import vietravel.example.vietravel.Model.Booking;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Data
public class TourScheduleDto {
    private Long id;
    private Long tourId;
    private LocalDateTime departureDate;
    private LocalTime departureTime;
    private LocalDateTime returnTime;
    private List<Long> bookingIds;
    private List<Long> guideIds;
}
