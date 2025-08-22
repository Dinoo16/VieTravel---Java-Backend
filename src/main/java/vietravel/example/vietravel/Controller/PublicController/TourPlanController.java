package vietravel.example.vietravel.Controller.PublicController;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vietravel.example.vietravel.Service.ServiceInterface.TourPlanService;
import vietravel.example.vietravel.dto.TourPlanDto;

import java.util.List;

@RestController
@RequestMapping("/api/public/tour-plans")
@RequiredArgsConstructor
public class TourPlanController {

    private final TourPlanService tourPlanService;

    // Get all tour plans
    @GetMapping
    public ResponseEntity<List<TourPlanDto>> getAllTourPlans() {
        List<TourPlanDto> tourPlans = tourPlanService.getAllTourPlan();
        return ResponseEntity.ok(tourPlans);
    }

}
