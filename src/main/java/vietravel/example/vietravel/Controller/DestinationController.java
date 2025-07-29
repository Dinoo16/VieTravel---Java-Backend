package vietravel.example.vietravel.Controller;


import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vietravel.example.vietravel.Service.DestinationService;
import vietravel.example.vietravel.dto.DestinationDto;

import java.util.List;

@RestController
@RequestMapping("/api/destinations")
@RequiredArgsConstructor
public class DestinationController {

    private final DestinationService destinationService;

    // Get all destinations
    @GetMapping
    public ResponseEntity<List<DestinationDto>> getAllDestinations() {
        List<DestinationDto> destinations = destinationService.getAllDestinations();
        return ResponseEntity.ok(destinations);
    }

    // Get destination by id
    @GetMapping("/{id}")
    public ResponseEntity<DestinationDto> getDestinationById(@PathVariable Long id) {
        DestinationDto destination = destinationService.getDestinationById(id);
        return ResponseEntity.ok(destination);
    }

    // Create destination
    @PostMapping
    public ResponseEntity<DestinationDto> createDestination(@RequestBody DestinationDto destinationDto) {
        DestinationDto destinations = destinationService.createDestination(destinationDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(destinations);

    }

    // Update destination
    @PutMapping("/{id}")
    public ResponseEntity<DestinationDto> updateDestination(@PathVariable Long id, @RequestBody DestinationDto destinationDto) {
        DestinationDto destination = destinationService.updateDestination(id, destinationDto);
        return ResponseEntity.ok(destination);
    }

    // Delete destination
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDestination(@PathVariable Long id) {
        destinationService.deleteDestination(id);
        return ResponseEntity.noContent().build();
    }


}
