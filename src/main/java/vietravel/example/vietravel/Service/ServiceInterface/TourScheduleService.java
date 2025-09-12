package vietravel.example.vietravel.Service.ServiceInterface;

import vietravel.example.vietravel.Model.Booking;
import vietravel.example.vietravel.dto.TourScheduleDetailDto;
import vietravel.example.vietravel.dto.TourScheduleDto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

public interface TourScheduleService {

    TourScheduleDto createDynamicSchedule(Long tourId, LocalDate departureDate, LocalTime departureTime, List<Long> guideIds, List<Long> bookingIds);
    TourScheduleDto getScheduleById(Long id);
    List<TourScheduleDto> getAllSchedules();

    Map<String, List<TourScheduleDetailDto>> getUserSchedules(Long userId);
}
