package vietravel.example.vietravel.Controller.PublicController;


import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vietravel.example.vietravel.Model.Destination;
import vietravel.example.vietravel.Service.DestinationService;
import vietravel.example.vietravel.dto.DestinationDto;
import vietravel.example.vietravel.dto.TourDto;

import java.util.List;

@RestController
@RequestMapping("/api/public/destinations")
@RequiredArgsConstructor
public class DestinationController {

    private final DestinationService destinationService;

//     Get all destinations
    @GetMapping
    public ResponseEntity<List<DestinationDto>> getAllDestinations() {
        List<DestinationDto> destinations = destinationService.getAllDestinations();
        return ResponseEntity.ok(destinations);
    }

//     Get destination by id
    @GetMapping("/{id}")
    public ResponseEntity<DestinationDto> getDestinationById(@PathVariable Long id) {
        DestinationDto destination = destinationService.getDestinationById(id);
        return ResponseEntity.ok(destination);
    }

    // Get all tours by destination id
    @GetMapping("/{id}/tours")
    public ResponseEntity<List<TourDto>> getToursByDestinationId(@PathVariable Long id, @RequestParam(required = false, defaultValue = "top") String sortBy ) {
        return ResponseEntity.ok(destinationService.getToursByDestinationId(id, sortBy));
    }

    // Top 5 popular destinations
    @GetMapping("/popular")
    public ResponseEntity<List<DestinationDto>> getPopularDestinations(
            @RequestParam(defaultValue = "6") int limit) {
        return ResponseEntity.ok(destinationService.getPopularDestinations(limit));
    }
}
