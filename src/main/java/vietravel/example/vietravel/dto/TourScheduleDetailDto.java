package vietravel.example.vietravel.dto;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
public class TourScheduleDetailDto {
    private Long id;
    private LocalDate departureDate;
    private LocalTime departureTime;
    private LocalDate returnTime;

    // Field from Tour
    private Long tourId;
    private String title;
    private String backgroundImage;
    private String destinationName;
    private Integer duration;

    // Field from booking
    private List<BookingSummaryDto> bookingSummaries;


    private List<String> guideNames;
}

