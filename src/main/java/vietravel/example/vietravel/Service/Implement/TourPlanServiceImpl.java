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

    // Get tour plan by tour id


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


    // Delete tour plan by id
    @Override
    public void deleteTourPlan(Long id) {
        tourPlanRepository.deleteById(id);
    }

    // Delete tour plan by tour id


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

        // Check if day is exists or not
        boolean isDuplicate = tourPlanRepository.existsByTourAndDay(tour, dto.getDay());
        if(isDuplicate) {
            throw new RuntimeException("Day " + dto.getDay() + " already exists in this tour");
        }

        return TourPlan.builder()
                .day(dto.getDay())
                .title(dto.getTitle())
                .content(dto.getContent())
                .tour(tour)
                .build();
    }
}
