package vietravel.example.vietravel.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vietravel.example.vietravel.Model.Tour;
import vietravel.example.vietravel.Model.TourSchedule;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

public interface TourScheduleRepository extends JpaRepository<TourSchedule, Long> {

    Optional<TourSchedule> findByTourTourId(Long id);

    List<TourSchedule> findByBookings_User_UserId(Long userId);

    Optional<TourSchedule> findByTourAndDepartureDateAndDepartureTime(Tour tour, LocalDate departureDate, LocalTime departureTime);

}
