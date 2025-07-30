package vietravel.example.vietravel.Controller.AdminController;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vietravel.example.vietravel.Service.TourScheduleService;
import vietravel.example.vietravel.dto.TourScheduleDto;

import java.util.List;

@RestController
@RequestMapping("/api/admin/schedules")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminTourScheduleController {

    private final TourScheduleService tourScheduleService;

    // Create a schedule
    @PostMapping
    public ResponseEntity<TourScheduleDto> createSchedule(@RequestBody TourScheduleDto dto) {
        TourScheduleDto created = tourScheduleService.createSchedule(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // Get all schedules
    @GetMapping
    public ResponseEntity<List<TourScheduleDto>> getAllSchedules() {
        List<TourScheduleDto> schedules = tourScheduleService.getAllSchedules();
        return ResponseEntity.ok(schedules);
    }

    // Get a schedule by ID
    @GetMapping("/{id}")
    public ResponseEntity<TourScheduleDto> getScheduleById(@PathVariable Long id) {
        TourScheduleDto schedule = tourScheduleService.getScheduleById(id);
        return ResponseEntity.ok(schedule);
    }

    // Update a schedule
    @PutMapping("/{id}")
    public ResponseEntity<TourScheduleDto> updateSchedule(
            @PathVariable Long id,
            @RequestBody TourScheduleDto dto
    ) {
        TourScheduleDto updated = tourScheduleService.updateSchedule(id, dto);
        return ResponseEntity.ok(updated);
    }

    // Delete a schedule
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSchedule(@PathVariable Long id) {
        tourScheduleService.deleteSchedule(id);
        return ResponseEntity.noContent().build();
    }
}
