package vietravel.example.vietravel.Service.Implement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vietravel.example.vietravel.Model.Tour;
import vietravel.example.vietravel.Model.TourPlan;
import vietravel.example.vietravel.Repository.TourPlanRepository;
import vietravel.example.vietravel.Repository.TourRepository;
import vietravel.example.vietravel.Service.ServiceInterface.TourPlanService;
import vietravel.example.vietravel.dto.TourPlanDto;

import java.util.ArrayList;
import java.util.List;
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
    public List<TourPlanDto> createMultipleTourPlans(List<TourPlanDto> dtos) {
        if (dtos.isEmpty()) {
            throw new RuntimeException("TourPlans list cannot be empty");
        }

        Long tourId = dtos.get(0).getTourId();
        Tour tour = tourRepository.findById(tourId)
                .orElseThrow(() -> new RuntimeException("Tour not found"));

        // Validate số lượng lịch trình phải đúng duration
        if (dtos.size() != tour.getDuration()) {
            throw new RuntimeException("This tour requires exactly " + tour.getDuration() + " days of TourPlan");
        }

        // Tạo danh sách entity, tự set day
        List<TourPlan> entities = new java.util.ArrayList<>();
        for (int i = 0; i < dtos.size(); i++) {
            TourPlanDto dto = dtos.get(i);

            // Nếu day này đã tồn tại thì báo lỗi
            if (tourPlanRepository.existsByTourAndDay(tour, i + 1)) {
                throw new RuntimeException("Day " + (i + 1) + " already exists in this tour");
            }

            entities.add(
                    TourPlan.builder()
                            .day(i + 1) // tự set từ 1 → duration
                            .title(dto.getTitle())
                            .content(dto.getContent())
                            .tour(tour)
                            .build()
            );
        }

        return tourPlanRepository.saveAll(entities)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }


    // Update multiple tour plans
    @Override
    public List<TourPlanDto> updateMultipleTourPlans(List<TourPlanDto> dtos) {
        if (dtos.isEmpty()) {
            throw new RuntimeException("TourPlans list cannot be empty");
        }

        List<TourPlanDto> result = new ArrayList<>();
        for (TourPlanDto dto : dtos) {
            if (dto.getId() == null) {
                throw new RuntimeException("Cannot update TourPlan without id");
            }

            TourPlan existing = tourPlanRepository.findById(dto.getId())
                    .orElseThrow(() -> new RuntimeException("TourPlan not found with id " + dto.getId()));

            // Update các field cần thiết
            existing.setTitle(dto.getTitle());
            existing.setContent(dto.getContent());

            result.add(toDto(tourPlanRepository.save(existing)));
        }

        return result;
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

        // Validate day with tour duration
        if (dto.getDay() < 1 || dto.getDay() > tour.getDuration()) {
            throw new RuntimeException(
                    "Invalid day " + dto.getDay() + ". This tour only has " + tour.getDuration() + " days."
            );
        }


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
