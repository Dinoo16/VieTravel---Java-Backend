package vietravel.example.vietravel.Service;

import vietravel.example.vietravel.dto.TourDto;
import vietravel.example.vietravel.dto.TourPlanDto;

import java.util.List;

public interface TourPlanService {

    List<TourPlanDto> getAllTourPlan();
    TourPlanDto createTourPlan(TourPlanDto dto);
    TourPlanDto updateTourPlan(Long id, TourPlanDto dto);

    void deleteTourPlan(Long id);


}
