package vietravel.example.vietravel.Service.Implement;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import vietravel.example.vietravel.Model.Destination;
import vietravel.example.vietravel.Model.Region;
import vietravel.example.vietravel.Model.Tour;
import vietravel.example.vietravel.Repository.DestinationRepository;
import vietravel.example.vietravel.Repository.RegionRepository;
import vietravel.example.vietravel.Service.DestinationService;
import vietravel.example.vietravel.dto.DestinationDto;

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

}
