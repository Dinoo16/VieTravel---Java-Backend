package vietravel.example.vietravel.Service.Implement;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import vietravel.example.vietravel.Model.*;
import vietravel.example.vietravel.Repository.*;
import vietravel.example.vietravel.Service.ServiceInterface.TourService;
import vietravel.example.vietravel.dto.*;
import vietravel.example.vietravel.util.TourSortUtil;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TourServiceImpl implements TourService {

    private final TourRepository tourRepository;
    private final CategoryRepository categoryRepository;
    private final DestinationRepository destinationRepository;
    private final GuideRepository guideRepository;
    private final TourPlanRepository tourPlanRepository;
    private final BookingRepository bookingRepository;


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
        String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().toUriString();
        dto.setBackgroundImage(baseUrl + tour.getBackgroundImage());
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
        System.out.println("Converting TourDto to Entity: " + dto);
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
    public List<TourDto> getTrendingTours(int limit) {
        List<Object[]> trending = bookingRepository.findTrendingTours();

        return trending.stream()
                .limit(limit)
                .map(obj -> {
                    Long tourId = (Long) obj[0]; // tourId ở cột đầu tiên
                    return tourRepository.findById(tourId).orElse(null);
                })
                .filter(Objects::nonNull)
                .map(this::toDto)
                .toList();
    }



    @Override
    public TourDto createTour(TourDto tourDto) {
        try {
            Tour tour = toEntity(tourDto);
            Tour savedTour = tourRepository.save(tour);
            return toDto(savedTour);
        } catch (Exception e) {
            // Log the error for debugging
            System.err.println("Error creating tour: " + e.getMessage());

            // Check for specific exceptions
            if (e.getCause() != null && e.getCause().getMessage().contains("Destination not found")) {
                throw new RuntimeException("Destination not found: " + tourDto.getDestinationName());
            } else if (e.getCause() != null && e.getCause().getMessage().contains("categories not found")) {
                throw new RuntimeException("One or more categories not found: " + tourDto.getCategoryNames());
            } else {
                throw new RuntimeException("Failed to create tour: " + e.getMessage());
            }
        }
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

        if (tourDto.getTourPlans() != null) {
            Map<Integer, TourPlan> existingPlans = tour.getTourPlans().stream()
                    .collect(Collectors.toMap(TourPlan::getDay, p -> p));

            Set<Integer> requestDays = tourDto.getTourPlans().stream()
                    .map(TourPlanDto::getDay)
                    .collect(Collectors.toSet());

            // Xóa plan không còn trong request
            tour.getTourPlans().removeIf(plan -> !requestDays.contains(plan.getDay()));

            // Thêm hoặc cập nhật
            for (TourPlanDto planDto : tourDto.getTourPlans()) {
                TourPlan plan = existingPlans.get(planDto.getDay());
                if (plan != null) {
                    plan.setTitle(planDto.getTitle());
                    plan.setContent(planDto.getContent());
                } else {
                    tour.getTourPlans().add(TourPlan.builder()
                            .day(planDto.getDay())
                            .title(planDto.getTitle())
                            .content(planDto.getContent())
                            .tour(tour)
                            .build());
                }
            }
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
    public List<TourDto> getAllTour(String sortBy) {
        List<Tour> tours = tourRepository.findAll();
        List<Tour> sortedTours = TourSortUtil.sortTours(tours, sortBy);

        return sortedTours
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

    // Search tours
    public List<TourDto> searchTours(String destination, Integer days, String category,
                                     Double minPrice, Double maxPrice, String sortBy) {
        List<Tour> tours = tourRepository.searchTours(destination, days, category, minPrice, maxPrice);
        List<Tour> sortedTours = TourSortUtil.sortTours(tours, sortBy);
        return sortedTours
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());

    }


//    @Override
//    public List<TourDto> getAllToursSorted(String sortBy) {
//        List<Tour> tours = tourRepository.findAll();
//
//        switch (sortBy.toLowerCase()) {
//            case "top":
//                // Sắp xếp số lượng tourSchedules giảm dần
//                tours.sort((t1, t2) -> Integer.compare(
//                        t2.getTourSchedules() != null ? t2.getTourSchedules().size() : 0,
//                        t1.getTourSchedules() != null ? t1.getTourSchedules().size() : 0
//                ));
//                break;
//
//            case "lowest":
//                // Sắp xếp price tăng dần
//                tours.sort((t1, t2) -> Double.compare(
//                        t1.getPrice() != null ? t1.getPrice() : 0.0,
//                        t2.getPrice() != null ? t2.getPrice() : 0.0
//                ));
//                break;
//
//            case "reviewed":
//                // TODO: sẽ implement sau dựa trên review
//                break;
//
//            default:
//                throw new RuntimeException("Invalid sort field: " + sortBy);
//        }
//
//        return tours.stream()
//                .map(this::toDto)
//                .collect(Collectors.toList());
//    }



}
