package vietravel.example.vietravel.Service.Implement;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vietravel.example.vietravel.Model.Guide;
import vietravel.example.vietravel.Model.Tour;
import vietravel.example.vietravel.Model.TourSchedule;
import vietravel.example.vietravel.Repository.GuideRepository;
import vietravel.example.vietravel.Repository.TourRepository;
import vietravel.example.vietravel.Repository.TourScheduleRepository;
import vietravel.example.vietravel.Service.TourScheduleService;
import vietravel.example.vietravel.dto.TourScheduleDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TourScheduleServiceImpl implements TourScheduleService {

    private final TourScheduleRepository scheduleRepository;
    private final TourRepository tourRepository;
    private final GuideRepository guideRepository;

    @Override
    public TourScheduleDto createDynamicSchedule(Long tourId, LocalDateTime departureDate, LocalDateTime returnDate, List<Long> guideIds) {
        Tour tour = tourRepository.findById(tourId)
                .orElseThrow(() -> new RuntimeException("Tour not found"));

        List<Guide> guides = guideRepository.findAllById(guideIds);

        TourSchedule schedule = TourSchedule.builder()
                .tour(tour)
                .departureDate(departureDate)
                .returnTime(returnDate)
                .guides(guides)
                .build();

        return toDto(scheduleRepository.save(schedule));
    }

    // Get schedule by id
    @Override
    public TourScheduleDto getScheduleById(Long id) {
        return scheduleRepository.findById(id)
                .map(this::toDto)
                .orElseThrow(() -> new RuntimeException("Schedule not found"));
    }

    // Get all schedule
    @Override
    public List<TourScheduleDto> getAllSchedules() {
        return scheduleRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());

    }

    private TourScheduleDto toDto(TourSchedule schedule) {
        TourScheduleDto dto = new TourScheduleDto();
        dto.setId(schedule.getId());
        dto.setTourId(schedule.getTour().getTourId());
        dto.setDepartureTime(schedule.getDepartureDate());
        dto.setReturnTime(schedule.getReturnTime());
        dto.setGuideIds(
                schedule.getGuides().stream()
                        .map(Guide::getId)
                        .collect(Collectors.toList())
        );
        return dto;
    }


}

