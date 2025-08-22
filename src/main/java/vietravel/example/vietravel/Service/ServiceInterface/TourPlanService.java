package vietravel.example.vietravel.Service.ServiceInterface;

import vietravel.example.vietravel.dto.TourDto;
import vietravel.example.vietravel.dto.TourPlanDto;

import java.util.List;

public interface TourPlanService {

    List<TourPlanDto> getAllTourPlan();

    void deleteTourPlan(Long id);

    List<TourPlanDto> createMultipleTourPlans(List<TourPlanDto> dtos);

    List<TourPlanDto> updateMultipleTourPlans(List<TourPlanDto> dtos);



}
