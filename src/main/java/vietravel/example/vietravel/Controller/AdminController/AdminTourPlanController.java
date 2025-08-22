package vietravel.example.vietravel.Controller.AdminController;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vietravel.example.vietravel.Service.ServiceInterface.TourPlanService;
import vietravel.example.vietravel.dto.TourPlanDto;

import java.util.List;

@RestController
@RequestMapping("/api/admin/tour-plans")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminTourPlanController {
    private final TourPlanService tourPlanService;

    // Update multiple tour plan
    @PutMapping("/multiple")
    public ResponseEntity<List<TourPlanDto>> updateMultipleTourPlans(@RequestBody List<TourPlanDto> dtos) {
        List<TourPlanDto> updatedPlans = tourPlanService.updateMultipleTourPlans(dtos);
        return ResponseEntity.ok(updatedPlans);
    }

    // Create multiple tour plan
    @PostMapping("/multiple")
    public ResponseEntity<List<TourPlanDto>> createMultipleTourPlans(@RequestBody List<TourPlanDto> dtos) {
        List<TourPlanDto> savedPlans = tourPlanService.createMultipleTourPlans(dtos);
        return ResponseEntity.ok(savedPlans);
    }


    // Get all tour plans
    @GetMapping
    public ResponseEntity<List<TourPlanDto>> getAllTourPlans() {
        List<TourPlanDto> tourPlans = tourPlanService.getAllTourPlan();
        return ResponseEntity.ok(tourPlans);
    }



    // Delete a tour plan
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTourPlan(@PathVariable Long id) {
        tourPlanService.deleteTourPlan(id);
        return ResponseEntity.noContent().build();
    }

}
