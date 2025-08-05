package vietravel.example.vietravel.Controller.AdminController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vietravel.example.vietravel.Service.TourService;
import vietravel.example.vietravel.dto.TourDto;
import vietravel.example.vietravel.dto.TourPlanDto;

import java.util.List;

@RestController
@RequestMapping("/api/admin/tours")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminTourController {
    private final TourService tourService;

    // Create tour
    @PostMapping
    public ResponseEntity<TourDto> createTour(@Valid @RequestBody TourDto tourDto) {
        TourDto createdTour = tourService.createTour(tourDto);
        return ResponseEntity.ok(createdTour);
    }
    // Update
    @PutMapping("/{id}")
    public ResponseEntity<TourDto> updateTour(@PathVariable Long id, @RequestBody TourDto tourDto) {
        TourDto updateTour = tourService.updateTour(id, tourDto);
        return ResponseEntity.ok(updateTour);
    }
    // Delete
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTour(@PathVariable Long id) {
        tourService.deleteTour(id);
        return ResponseEntity.noContent().build();
    }

    // Get all tours
    @GetMapping
    public ResponseEntity<List<TourDto>> getAllTours() {
        List<TourDto> tours = tourService.getAllTour();
        return ResponseEntity.ok(tours);
    }

    // Get tour by id
    @GetMapping("/{id}")
    public ResponseEntity<TourDto> getTourById(@PathVariable Long id) {
        TourDto tour = tourService.getTourById(id);
        return ResponseEntity.ok(tour);
    }

    // Get tour plans by tour id
    @GetMapping("/{id}/tour-plans")
    public ResponseEntity<List<TourPlanDto>> getTourPlansByTourId(@PathVariable Long id) {
        List<TourPlanDto> tourPlans = tourService.getTourPlansByTourId(id);
        return ResponseEntity.ok(tourPlans);
    }

}

