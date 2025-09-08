package vietravel.example.vietravel.Service.ServiceInterface;

import vietravel.example.vietravel.Model.Booking;
import vietravel.example.vietravel.dto.TourScheduleDto;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public interface TourScheduleService {

    TourScheduleDto createDynamicSchedule(Long tourId, LocalDateTime departureDate, LocalTime departureTime, List<Long> guideIds, List<Long> bookingIds);
    TourScheduleDto getScheduleById(Long id);
    List<TourScheduleDto> getAllSchedules();
}
