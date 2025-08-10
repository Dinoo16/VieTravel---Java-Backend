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


}
