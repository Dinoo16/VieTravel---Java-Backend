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

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TourScheduleServiceImpl implements TourScheduleService {

    private final TourScheduleRepository scheduleRepository;
    private final TourRepository tourRepository;
    private final GuideRepository guideRepository;

    @Override
    public TourScheduleDto createSchedule(TourScheduleDto dto) {
        TourSchedule schedule = toEntity(dto);
        return toDto(scheduleRepository.save(schedule));
    }

    @Override
    public TourScheduleDto getScheduleById(Long id) {
        TourSchedule schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Schedule not found"));
        return toDto(schedule);
    }

    @Override
    public List<TourScheduleDto> getAllSchedules() {
        return scheduleRepository.findAll()
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public TourScheduleDto updateSchedule(Long id, TourScheduleDto dto) {
        TourSchedule existing = scheduleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Schedule not found"));

        Tour tour = tourRepository.findById(dto.getTourId())
                .orElseThrow(() -> new RuntimeException("Tour not found"));

        List<Guide> guides = guideRepository.findAllById(dto.getGuideIds());

        existing.setTour(tour);
        existing.setDepartureDate(dto.getDepartureTime());
        existing.setReturnTime(dto.getReturnTime());
        existing.setGuides(guides);

        return toDto(scheduleRepository.save(existing));
    }

    @Override
    public void deleteSchedule(Long id) {
        if (!scheduleRepository.existsById(id)) {
            throw new RuntimeException("Schedule not found");
        }
        scheduleRepository.deleteById(id);
    }

    private TourSchedule toEntity(TourScheduleDto dto) {
        Tour tour = tourRepository.findById(dto.getTourId())
                .orElseThrow(() -> new RuntimeException("Tour not found"));

        List<Guide> guides = guideRepository.findAllById(dto.getGuideIds());

        return TourSchedule.builder()
                .tour(tour)
                .departureDate(dto.getDepartureTime())
                .returnTime(dto.getReturnTime())
                .guides(guides)
                .build();
    }

    private TourScheduleDto toDto(TourSchedule schedule) {
        TourScheduleDto dto = new TourScheduleDto();
        dto.setId(schedule.getId());
        dto.setTourId(schedule.getTour().getTourId());
        dto.setDepartureTime(schedule.getDepartureDate());
        dto.setReturnTime(schedule.getReturnTime());
        dto.setGuideIds(
                schedule.getGuides().stream()
                        .map(guide -> guide.getId())
                        .collect(Collectors.toList())
        );
        return dto;
    }
}
