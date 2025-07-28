package vietravel.example.vietravel.Service.Implement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vietravel.example.vietravel.Model.Tour;
import vietravel.example.vietravel.Model.TourPlan;
import vietravel.example.vietravel.Repository.TourPlanRepository;
import vietravel.example.vietravel.Repository.TourRepository;
import vietravel.example.vietravel.Service.TourPlanService;
import vietravel.example.vietravel.dto.TourPlanDto;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TourPlanServiceImpl implements TourPlanService {

    @Autowired
    private TourPlanRepository tourPlanRepository;

    @Autowired
    private TourRepository tourRepository;

    @Override
    public List<TourPlanDto> getAllTourPlan() {
        return tourPlanRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public TourPlanDto createTourPlan(TourPlanDto dto) {
        TourPlan entity = toEntity(dto);
        TourPlan saved = tourPlanRepository.save(entity);
        return toDto(saved);
    }

    @Override
    public TourPlanDto updateTourPlan(Long id, TourPlanDto dto) {
        Optional<TourPlan> optionalTourPlan = tourPlanRepository.findById(id);
        if (optionalTourPlan.isPresent()) {
            TourPlan existing = optionalTourPlan.get();
            existing.setDay(dto.getDay());
            existing.setTitle(dto.getTitle());
            existing.setContent(dto.getContent());

            Tour tour = tourRepository.findById(dto.getTourId())
                    .orElseThrow(() -> new RuntimeException("Tour not found"));
            existing.setTour(tour);

            TourPlan updated = tourPlanRepository.save(existing);
            return toDto(updated);
        }
        throw new RuntimeException("TourPlan not found with id: " + id);
    }

    @Override
    public void deleteTourPlan(Long id) {
        tourPlanRepository.deleteById(id);
    }

    // Convert entity to DTO
    private TourPlanDto toDto(TourPlan tourPlan) {
        TourPlanDto dto = new TourPlanDto();
        dto.setId(tourPlan.getId());
        dto.setDay(tourPlan.getDay());
        dto.setTitle(tourPlan.getTitle());
        dto.setContent(tourPlan.getContent());
        dto.setTourId(tourPlan.getTour().getTourId());
        return dto;
    }

    // Convert DTO to entity
    private TourPlan toEntity(TourPlanDto dto) {
        Tour tour = tourRepository.findById(dto.getTourId())
                .orElseThrow(() -> new RuntimeException("Tour not found"));

        return TourPlan.builder()
                .day(dto.getDay())
                .title(dto.getTitle())
                .content(dto.getContent())
                .tour(tour)
                .build();
    }
}
