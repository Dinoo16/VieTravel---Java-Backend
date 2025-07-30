package vietravel.example.vietravel.Controller.UserController;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vietravel.example.vietravel.Service.TourPlanService;
import vietravel.example.vietravel.dto.TourPlanDto;

import java.util.List;

@RestController
@RequestMapping("/api/tour-plans")
@RequiredArgsConstructor
@PreAuthorize("hasRole('USER') or hasRole('GUIDE')")
public class TourPlanController {

    private final TourPlanService tourPlanService;

    // Get all tour plans
    @GetMapping
    public ResponseEntity<List<TourPlanDto>> getAllTourPlans() {
        List<TourPlanDto> tourPlans = tourPlanService.getAllTourPlan();
        return ResponseEntity.ok(tourPlans);
    }

}
