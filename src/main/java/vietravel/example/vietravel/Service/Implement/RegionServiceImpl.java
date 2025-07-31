package vietravel.example.vietravel.Service.Implement;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vietravel.example.vietravel.Model.Region;
import vietravel.example.vietravel.Repository.RegionRepository;
import vietravel.example.vietravel.Service.RegionService;
import vietravel.example.vietravel.dto.DestinationDto;
import vietravel.example.vietravel.dto.RegionDto;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RegionServiceImpl implements RegionService {
    private final RegionRepository regionRepository;
    @Override
    public List<RegionDto> getAllRegions() {
        List<Region> regions = regionRepository.findAll();
        return regions.stream().map(this::toDto).collect(Collectors.toList());
    }

    @Override
    public RegionDto getRegionById(Long id) {
        Region region = regionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Region not found with id: " + id));
        return toDto(region);
    }

    private RegionDto toDto(Region region) {
        RegionDto dto = new RegionDto();
        dto.setRegionId(region.getRegionId());
        dto.setName(region.getName());
        dto.setDescription(region.getDescription());

        if (region.getDestinations() != null) {
            List<DestinationDto> destinationDtos = region.getDestinations().stream().map(destination -> {
                DestinationDto destinationDto = DestinationDto.builder()
                        .id(destination.getDestinationId())
                        .name(destination.getName())
                        .description(destination.getDescription())
                        .regionId(region.getRegionId())
                        .backgroundImage(destination.getBackgroundImage())
                        .build();
                return destinationDto;
            }).collect(Collectors.toList());

            dto.setDestinations(destinationDtos);
        }

        return dto;
    }
}
