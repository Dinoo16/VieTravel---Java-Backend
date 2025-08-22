package vietravel.example.vietravel.Service.Implement;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import vietravel.example.vietravel.Model.Category;
import vietravel.example.vietravel.Model.Destination;
import vietravel.example.vietravel.Model.Region;
import vietravel.example.vietravel.Model.Tour;
import vietravel.example.vietravel.Repository.DestinationRepository;
import vietravel.example.vietravel.Repository.RegionRepository;
import vietravel.example.vietravel.Service.ServiceInterface.DestinationService;
import vietravel.example.vietravel.dto.DestinationDto;
import vietravel.example.vietravel.dto.ReviewDto;
import vietravel.example.vietravel.dto.TourDto;
import vietravel.example.vietravel.dto.TourPlanDto;
import vietravel.example.vietravel.util.TourSortUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class DestinationServiceImpl implements DestinationService {

    private final DestinationRepository destinationRepository;
    private final RegionRepository regionRepository;


    private DestinationDto toDto(Destination destination) {
        List<Long> tourIds = destination.getTours() != null ?
                destination.getTours().stream().map(Tour::getTourId).toList() : null;
        String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().toUriString();
        return DestinationDto.builder()
                .id(destination.getDestinationId())
                .name(destination.getName())
                .description(destination.getDescription())
                .backgroundImage(baseUrl + destination.getBackgroundImage())
                .regionId(destination.getRegion().getRegionId())
                .tourIds(tourIds)
                .build();
    }


    @Override
    public DestinationDto createDestination(DestinationDto destinationDto) {
        Region region = regionRepository.findById(destinationDto.getRegionId())
                .orElseThrow(() -> new RuntimeException("Region not found"));
        if(destinationRepository.existsByNameIgnoreCase(destinationDto.getName())) {
            throw new IllegalArgumentException("Destination name already exists: " + destinationDto.getName());
        }

        Destination destination = Destination.builder()
                .name(destinationDto.getName())
                .description(destinationDto.getDescription())
                .region(region)
                .backgroundImage(destinationDto.getBackgroundImage())
                .build();
        return toDto(destinationRepository.save(destination));

    }

    @Override
    public DestinationDto updateDestination(Long id, DestinationDto destinationDto) {
        Destination destination = destinationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Destination not found"));

        if (destinationDto.getName() != null &&
                !destinationDto.getName().equalsIgnoreCase(destination.getName()) &&
                destinationRepository.existsByNameIgnoreCase(destinationDto.getName())) {
            throw new IllegalArgumentException("Destination name already exists: " + destinationDto.getName());
        }

        if (destinationDto.getName() != null) {
            destination.setName(destinationDto.getName());
        }
        if (destinationDto.getDescription() != null) {
            destination.setDescription(destinationDto.getDescription());
        }

        if (destinationDto.getRegionId() != null) {
            Region region = regionRepository.findById(destinationDto.getRegionId())
                    .orElseThrow(() -> new RuntimeException("Region not found"));
            destination.setRegion(region);
        }
        if (destinationDto.getBackgroundImage() != null) {
            destination.setBackgroundImage(destinationDto.getBackgroundImage());
        }

        return toDto(destinationRepository.save(destination));
    }

    @Override
    public void deleteDestination(Long id) {
        if(!destinationRepository.existsById(id)) {
            throw new RuntimeException("Destination not found");
        }
        destinationRepository.deleteById(id);
    }

    @Override
    public List<DestinationDto> getAllDestinations() {
        return destinationRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public DestinationDto getDestinationById(Long id) {
        Destination destination = destinationRepository.findById(id).
                orElseThrow(() -> new RuntimeException("Destination not found"));

        return toDto(destination);
    }

    // Get top 5 popular destinations
    public List<DestinationDto> getPopularDestinations(int limit) {
        return destinationRepository.findPopularDestinations(PageRequest.of(0, limit))
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    // Get all tours by destination id
    @Override
    public List<TourDto> getToursByDestinationId(Long id, String sortBy) {
        Destination destination = destinationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Destination not found"));

        // Sort tour
        List<Tour> sortedTours = TourSortUtil.sortTours(destination.getTours(), sortBy);

        // Convert list tours -> tourDto
        return sortedTours.stream()
                .map(this::toTourDto)
                .collect(Collectors.toList());
    }
    private TourDto toTourDto(Tour tour) {
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


}
