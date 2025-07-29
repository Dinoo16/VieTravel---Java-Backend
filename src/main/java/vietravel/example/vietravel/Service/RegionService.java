package vietravel.example.vietravel.Service;

import vietravel.example.vietravel.dto.RegionDto;

import java.util.List;

public interface RegionService {
    List<RegionDto> getAllRegions();
    RegionDto getRegionById(Long id);
}
