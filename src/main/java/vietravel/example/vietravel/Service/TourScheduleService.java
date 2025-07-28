package vietravel.example.vietravel.Service;

import vietravel.example.vietravel.dto.TourScheduleDto;

import java.util.List;

public interface TourScheduleService {

    TourScheduleDto createSchedule(TourScheduleDto dto);

    TourScheduleDto getScheduleById(Long id);

    List<TourScheduleDto> getAllSchedules();

    TourScheduleDto updateSchedule(Long id, TourScheduleDto dto);

    void deleteSchedule(Long id);
}
