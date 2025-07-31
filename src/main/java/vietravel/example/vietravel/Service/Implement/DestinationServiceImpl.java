package vietravel.example.vietravel.Service.Implement;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vietravel.example.vietravel.Model.Destination;
import vietravel.example.vietravel.Model.Region;
import vietravel.example.vietravel.Repository.DestinationRepository;
import vietravel.example.vietravel.Service.DestinationService;
import vietravel.example.vietravel.dto.DestinationDto;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class DestinationServiceImpl implements DestinationService {

    private final DestinationRepository destinationRepository;


    private DestinationDto toDto(Destination destination) {
        return DestinationDto.builder()
                .id(destination.getDestinationId())
                .name(destination.getName())
                .description(destination.getDescription())
                .backgroundImage(destination.getBackgroundImage())
                .regionId(destination.getRegion().getRegionId())
                .build();
    }


    @Override
    public DestinationDto createDestination(DestinationDto destinationDto) {
        Destination destination = Destination.builder()
                .name(destinationDto.getName())
                .description(destinationDto.getDescription())
                .region(Region.builder().regionId(destinationDto.getRegionId()).build())
                .backgroundImage(destinationDto.getBackgroundImage()).build();
        return toDto(destinationRepository.save(destination));

    }

    @Override
    public DestinationDto updateDestination(Long id, DestinationDto destinationDto) {
        Destination destination = destinationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Destination not found"));
        destination.setName(destinationDto.getName());
        destination.setDescription(destinationDto.getDescription());
        destination.setRegion(Region.builder().regionId(destinationDto.getRegionId()).build());
        destination.setBackgroundImage(destinationDto.getBackgroundImage());

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
