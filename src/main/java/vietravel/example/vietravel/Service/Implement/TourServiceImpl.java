package vietravel.example.vietravel.Service.Implement;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vietravel.example.vietravel.Model.*;
import vietravel.example.vietravel.Repository.*;
import vietravel.example.vietravel.Service.TourService;
import vietravel.example.vietravel.dto.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TourServiceImpl implements TourService {

    private final TourRepository tourRepository;
    private final CategoryRepository categoryRepository;
    private final DestinationRepository destinationRepository;
    private final GuideRepository guideRepository;
    private final TourPlanRepository tourPlanRepository;

    private TourDto toDto(Tour tour) {
        TourDto dto = new TourDto();
        dto.setId(tour.getTourId());
        dto.setTitle(tour.getTitle());
        dto.setDestinationName(tour.getDestination().getName());
        dto.setDeparture(tour.getDeparture());
        dto.setDepartureTime(tour.getDepartureTime());
        dto.setReturnTime(tour.getReturnTime());
        dto.setCategoryNames(tour.getCategories().stream().map(Category::getName).collect(Collectors.toList()));
        dto.setGuideName(tour.getGuide() != null ? tour.getGuide().getName() : null);
        dto.setDuration(tour.getDuration() + (tour.getDuration() == 1 ? " day" : " days"));
        dto.setPrice(tour.getPrice());
        dto.setDescription(tour.getDescription());
        dto.setBackgroundImage(tour.getBackgroundImage());
        dto.setGallery(tour.getGallery());
        dto.setTourPlans(
                tour.getTourPlans() != null ?
                        tour.getTourPlans().stream().map(plan -> {
                            TourPlanDto planDto = new TourPlanDto();
                            planDto.setId(plan.getId());
                            planDto.setTourId(plan.getTour().getTourId());
                            planDto.setDay(plan.getDay());
                            planDto.setTitle(plan.getTitle());
                            planDto.setContent(plan.getContent());
                            return planDto;
                        }).collect(Collectors.toList())
                        : new ArrayList<>()
        );
        if (tour.getReviews() != null) {
            dto.setReviews(
                    tour.getReviews().stream()
                            .map(review -> {
                                ReviewDto reviewDto = new ReviewDto();
                                reviewDto.setId(review.getId());
                                reviewDto.setUserId(review.getUser().getUserId());
                                reviewDto.setTourId(review.getTour().getTourId());
                                reviewDto.setRating(review.getRating());
                                reviewDto.setComment(review.getComment());
                                return reviewDto;
                            })
                            .collect(Collectors.toList())
            );
        } else {
            dto.setReviews(new ArrayList<>());
        }

        return dto;
    }

    public Tour toEntity(TourDto dto) {
        // Fetch Destination
        Destination destination = destinationRepository.findByName(dto.getDestinationName())
                .orElseThrow(() -> new RuntimeException("Destination not found"));

        // Fetch Categories
        List<Category> categories = categoryRepository.findByNameIn(dto.getCategoryNames());
        if (categories.size() != dto.getCategoryNames().size()) {
            throw new RuntimeException("One or more categories not found");
        }

        // Fetch Guide
        Guide guide = null;
        if (dto.getGuideName() != null && !dto.getGuideName().isBlank()) {
            guide = guideRepository.findByName(dto.getGuideName())
                    .orElseThrow(() -> new RuntimeException("Guide not found"));
        }
//        Guide guide = guideRepository.findByName(dto.getGuideName())
//                .orElseThrow(() -> new RuntimeException("Guide not found"));

        // Check if day is exists or not
        Set<Integer> days = new HashSet<>();
        if (dto.getTourPlans() != null) {
            for (TourPlanDto planDto : dto.getTourPlans()) {
                if (!days.add(planDto.getDay())) {
                    throw new RuntimeException("Duplicate day found in tour plans: day " + planDto.getDay());
                }
            }
        }

        // Convert TourPlans
        List<TourPlan> tourPlans = dto.getTourPlans() != null
                ? dto.getTourPlans().stream()
                .map(planDto -> TourPlan.builder()
                        .day(planDto.getDay())
                        .title(planDto.getTitle())
                        .content(planDto.getContent())
                        .build())
                .collect(Collectors.toList())
                : new ArrayList<>();

        // Build Tour
        Tour tour = Tour.builder()
                .title(dto.getTitle())
                .departure(dto.getDeparture())
                .departureTime(dto.getDepartureTime())
                .returnTime(dto.getReturnTime())
                .duration(Integer.parseInt(dto.getDuration().replaceAll("\\D", "")))
                .price(dto.getPrice())
                .description(dto.getDescription())
                .backgroundImage(dto.getBackgroundImage())
                .gallery(dto.getGallery())
                .availableDates(dto.getAvailableDates())
                .destination(destination)
                .categories(categories)
                .guide(guide)
                .tourPlans(new ArrayList<>()) // Temporary

                .build();


        // Link each TourPlan back to Tour
        tourPlans.forEach(plan -> plan.setTour(tour));
        tour.setTourPlans(tourPlans);

        return tour;

    }


    @Override
    public TourDto createTour(TourDto tourDto) {
        Tour tour = toEntity(tourDto);
        return toDto(tourRepository.save(tour));

    }

    @Override
    @Transactional
    public TourDto updateTour(Long id, TourDto tourDto) {
        Tour tour = tourRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tour not found"));

        Destination destination = destinationRepository.findByName(tourDto.getDestinationName())
                .orElseThrow(() -> new RuntimeException("Destination not found"));

        List<Category> categories = categoryRepository.findByNameIn(tourDto.getCategoryNames());
        if (categories.size() != tourDto.getCategoryNames().size()) {
            throw new RuntimeException("One or more categories not found");
        }

        Guide guide = null;
        if (tourDto.getGuideName() != null && !tourDto.getGuideName().isBlank()) {
            guide = guideRepository.findByName(tourDto.getGuideName())
                    .orElseThrow(() -> new RuntimeException("Guide not found"));
        }

        // Clear existing tourPlans and add new ones
        tour.getTourPlans().clear();
        if (tourDto.getTourPlans() != null) {
            tourDto.getTourPlans().forEach(planDto -> {
                TourPlan plan = TourPlan.builder()
                        .day(planDto.getDay())
                        .title(planDto.getTitle())
                        .content(planDto.getContent())
                        .tour(tour) // Set the parent tour
                        .build();
                tour.getTourPlans().add(plan); // ✅ Adds to the existing collection
            });
        }

        // Cập nhật các trường
        tour.setTitle(tourDto.getTitle());
        tour.setDeparture(tourDto.getDeparture());
        tour.setDepartureTime(tourDto.getDepartureTime());
        tour.setReturnTime(tourDto.getReturnTime());
        tour.setDuration(Integer.parseInt(tourDto.getDuration().replaceAll("\\D", "")));
        tour.setPrice(tourDto.getPrice());
        tour.setDescription(tourDto.getDescription());
        tour.setBackgroundImage(tourDto.getBackgroundImage());
        tour.setGallery(tourDto.getGallery());
        tour.setAvailableDates(tourDto.getAvailableDates());
        tour.setDestination(destination);
        tour.setCategories(categories);
        tour.setGuide(guide);
        Tour updatedTour = tourRepository.save(tour);
        return toDto(updatedTour);
    }


    @Override
    public void deleteTour(Long id) {
        if (!tourRepository.existsById(id)) {
            throw new RuntimeException("Tour not found");
        }
        tourRepository.deleteById(id);
    }


    @Override
    public List<TourDto> getAllTour() {
        return tourRepository.findAll()
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }


    @Override
    public TourDto getTourById(Long id) {
        Tour tour = tourRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tour not found"));
        return toDto(tour);
    }


    // Get tour plans by tour id
    @Override
    public List<TourPlanDto> getTourPlansByTourId(Long id) {
        return tourPlanRepository.findByTour_TourId(id).stream().map(this::tourPlanDto).collect(Collectors.toList());
    }

    // Convert tour plan entity to DTO
    private TourPlanDto tourPlanDto(TourPlan tourPlan) {
        TourPlanDto dto = new TourPlanDto();
        dto.setId(tourPlan.getId());
        dto.setDay(tourPlan.getDay());
        dto.setTitle(tourPlan.getTitle());
        dto.setContent(tourPlan.getContent());
        dto.setTourId(tourPlan.getTour().getTourId());
        return dto;
    }

}
