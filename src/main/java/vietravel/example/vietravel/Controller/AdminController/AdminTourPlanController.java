package vietravel.example.vietravel.Controller.AdminController;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vietravel.example.vietravel.Model.Tour;
import vietravel.example.vietravel.Service.TourPlanService;
import vietravel.example.vietravel.dto.TourPlanDto;

import java.util.List;

@RestController
@RequestMapping("/api/admin/tour-plans")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminTourPlanController {
    private final TourPlanService tourPlanService;

    // Get all tour plans
    @GetMapping
    public ResponseEntity<List<TourPlanDto>> getAllTourPlans() {
        List<TourPlanDto> tourPlans = tourPlanService.getAllTourPlan();
        return ResponseEntity.ok(tourPlans);
    }


    // Create a tour plan
    @PostMapping
    public ResponseEntity<TourPlanDto> createTourPlan(@RequestBody TourPlanDto dto) {
        TourPlanDto created = tourPlanService.createTourPlan(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // Update a tour plan
    @PutMapping("/{id}")
    public ResponseEntity<TourPlanDto> updateTourPlan(
            @PathVariable Long id,
            @RequestBody TourPlanDto dto
    ) {
        TourPlanDto updated = tourPlanService.updateTourPlan(id, dto);
        return ResponseEntity.ok(updated);
    }

    // Delete a tour plan
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTourPlan(@PathVariable Long id) {
        tourPlanService.deleteTourPlan(id);
        return ResponseEntity.noContent().build();
    }

}
