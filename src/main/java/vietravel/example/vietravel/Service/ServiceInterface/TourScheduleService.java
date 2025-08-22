package vietravel.example.vietravel.Service.ServiceInterface;

import vietravel.example.vietravel.dto.TourScheduleDto;

import java.time.LocalDateTime;
import java.util.List;

public interface TourScheduleService {

    TourScheduleDto createDynamicSchedule(Long tourId, LocalDateTime departureDate, LocalDateTime returnDate, List<Long> guideIds);
    TourScheduleDto getScheduleById(Long id);
    List<TourScheduleDto> getAllSchedules();
}
