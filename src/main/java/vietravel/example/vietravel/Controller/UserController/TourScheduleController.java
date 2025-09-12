package vietravel.example.vietravel.Controller.UserController;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import vietravel.example.vietravel.Model.MyUserDetails;
import vietravel.example.vietravel.Service.ServiceInterface.TourScheduleService;
import vietravel.example.vietravel.dto.BookingDto;
import vietravel.example.vietravel.dto.TourScheduleDetailDto;
import vietravel.example.vietravel.dto.TourScheduleDto;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/user/tour-schedules")
@RequiredArgsConstructor

public class TourScheduleController {

    private final TourScheduleService tourScheduleService;

    // User get all tour schedule
    @GetMapping
    public ResponseEntity<Map<String, List<TourScheduleDetailDto>>> getUserSchedules(Authentication authentication) {
        MyUserDetails userDetails = (MyUserDetails) authentication.getPrincipal();
        Long userId = userDetails.getId();
        return ResponseEntity.ok(tourScheduleService.getUserSchedules(userId));
    }

}

