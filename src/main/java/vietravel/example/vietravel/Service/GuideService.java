package vietravel.example.vietravel.Service;

import vietravel.example.vietravel.dto.GuideDto;

import java.util.List;

public interface GuideService {
    GuideDto createGuide(GuideDto dto);
    GuideDto updateGuide(Long id, GuideDto dto);
    void deleteGuide(Long id);
    GuideDto getGuideById(Long id);
    List<GuideDto> getAllGuides();
}
