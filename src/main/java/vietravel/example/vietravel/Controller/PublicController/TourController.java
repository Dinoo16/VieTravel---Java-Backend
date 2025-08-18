package vietravel.example.vietravel.Controller.PublicController;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vietravel.example.vietravel.Service.TourService;
import vietravel.example.vietravel.dto.TourDto;

import java.util.List;

@RestController
@RequestMapping("/api/public/tours")
@RequiredArgsConstructor
public class TourController {

    private final TourService tourService;

    // Get all tours
    @GetMapping
    public ResponseEntity<List<TourDto>> getAllTours(@RequestParam(required = false, defaultValue = "top") String sortBy) {
        List<TourDto> tours = tourService.getAllTour(sortBy);
        return ResponseEntity.ok(tours);
    }

    // Get tour by id
    @GetMapping("/{id}")
    public ResponseEntity<TourDto> getTourById(@PathVariable Long id) {
        TourDto tour = tourService.getTourById(id);
        return ResponseEntity.ok(tour);
    }

    @GetMapping("/search")
    public ResponseEntity<List<TourDto>> searchTours(
            @RequestParam(required = false) String destination,
            @RequestParam(required = false) Integer days,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false, defaultValue = "top") String sortBy
    ) {
        return ResponseEntity.ok(
                tourService.searchTours(destination, days, category, minPrice, maxPrice, sortBy)
        );
    }



}
