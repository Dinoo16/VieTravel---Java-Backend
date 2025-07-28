package vietravel.example.vietravel.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class TourScheduleDto {
    private Long id;
    private Long tourId;
    private LocalDateTime departureTime;
    private LocalDateTime returnTime;
    private List<Long> guideIds;
}
