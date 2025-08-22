package vietravel.example.vietravel.Controller.UserController;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vietravel.example.vietravel.Service.ServiceInterface.TourScheduleService;
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

