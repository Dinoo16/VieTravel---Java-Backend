package vietravel.example.vietravel.Service.Implement;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vietravel.example.vietravel.Model.*;
import vietravel.example.vietravel.Repository.CategoryRepository;
import vietravel.example.vietravel.Repository.DestinationRepository;
import vietravel.example.vietravel.Repository.GuideRepository;
import vietravel.example.vietravel.Repository.TourRepository;
import vietravel.example.vietravel.Service.TourService;
import vietravel.example.vietravel.dto.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TourServiceImpl implements TourService {

    private final TourRepository tourRepository;
    private final CategoryRepository categoryRepository;
    private final DestinationRepository destinationRepository;
    private final GuideRepository guideRepository;

    private TourDto toDto(Tour tour) {
        TourDto dto = new TourDto();
        dto.setId(tour.getTourId());
        dto.setDestinationName(tour.getDestination().getName());
        dto.setDeparture(tour.getDeparture());
        dto.setDepartureTime(tour.getDepartureTime());
        dto.setReturnTime(tour.getReturnTime());
        dto.setCategoryNames(tour.getCategories().stream().map(Category::getName).collect(Collectors.toList()));
        dto.setGuideName(tour.getGuide().getName());
        dto.setDuration(tour.getDuration() + (tour.getDuration() == 1 ? " day" : " days"));
        dto.setPrice(tour.getPrice());
        dto.setDescription(tour.getDescription());
        dto.setBackgroundImage(tour.getBackgroundImage());
        dto.setGallery(tour.getGallery());
        dto.setTourPlans(tour.getTourPlans().stream().map(plan -> {
            TourPlanDto planDto = new TourPlanDto();
            planDto.setDay(plan.getDay());
            planDto.setTitle(plan.getTitle());
            planDto.setContent(plan.getContent());
            return planDto;
        }).collect(Collectors.toList()));
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
        Guide guide = guideRepository.findByName(dto.getGuideName())
                .orElseThrow(() -> new RuntimeException("Guide not found"));

        // Convert TourPlans
        List<TourPlan> tourPlans = dto.getTourPlans().stream()
                .map(planDto -> TourPlan.builder()
                        .day(planDto.getDay())
                        .title(planDto.getTitle())
                        .content(planDto.getContent())
                        .build())
                .collect(Collectors.toList());

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
    public TourDto updateTour(Long id, TourDto tourDto) {
        Tour tour = tourRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tour not found"));

        Destination destination = destinationRepository.findByName(tourDto.getDestinationName())
                .orElseThrow(() -> new RuntimeException("Destination not found"));

        List<Category> categories = categoryRepository.findByNameIn(tourDto.getCategoryNames());
        if (categories.size() != tourDto.getCategoryNames().size()) {
            throw new RuntimeException("One or more categories not found");
        }

        Guide guide = guideRepository.findByName(tourDto.getGuideName())
                .orElseThrow(() -> new RuntimeException("Guide not found"));

        // Xóa các kế hoạch cũ và tạo lại từ DTO
        List<TourPlan> tourPlans = tourDto.getTourPlans().stream()
                .map(planDto -> TourPlan.builder()
                        .day(planDto.getDay())
                        .title(planDto.getTitle())
                        .content(planDto.getContent())
                        .tour(tour)
                        .build())
                .collect(Collectors.toList());

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
        tour.setTourPlans(tourPlans);

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

}
