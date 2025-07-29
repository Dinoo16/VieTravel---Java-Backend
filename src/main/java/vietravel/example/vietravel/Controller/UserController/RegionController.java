package vietravel.example.vietravel.Controller.UserController;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vietravel.example.vietravel.Service.RegionService;
import vietravel.example.vietravel.dto.RegionDto;

import java.util.List;

@RestController
@RequestMapping("/api/regions")
@RequiredArgsConstructor
@PreAuthorize("hasRole('USER') or hasRole('GUIDE')")
public class RegionController {
    private final RegionService regionService;

    @GetMapping
    public ResponseEntity<List<RegionDto>> getAllRegions() {
        return ResponseEntity.ok(regionService.getAllRegions());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RegionDto> getRegionById(@PathVariable Long id) {
        return ResponseEntity.ok(regionService.getRegionById(id));
    }
}
