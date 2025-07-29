package vietravel.example.vietravel.Controller.UserController;


import org.springframework.security.access.prepost.PreAuthorize;

@PreAuthorize("hasRole('USER') or hasRole('GUIDE')")
public class TourScheduleController {
}
