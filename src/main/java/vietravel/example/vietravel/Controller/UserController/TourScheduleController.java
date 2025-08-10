package vietravel.example.vietravel.Controller.UserController;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vietravel.example.vietravel.Service.TourScheduleService;
import vietravel.example.vietravel.dto.TourScheduleDto;

@RestController
@RequestMapping("/api/user/tour-schedules")
@RequiredArgsConstructor

public class TourScheduleController {

    private final TourScheduleService tourScheduleService;

    @GetMapping("/{id}")
    public ResponseEntity<TourScheduleDto> getSchedule(@PathVariable Long id) {
        return ResponseEntity.ok(tourScheduleService.getScheduleById(id));
    }
}

