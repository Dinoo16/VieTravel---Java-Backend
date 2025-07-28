package vietravel.example.vietravel.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vietravel.example.vietravel.Model.TourSchedule;

import java.util.*;

public interface TourScheduleRepository extends JpaRepository<TourSchedule, Long> {

    Optional<TourSchedule> findById(Long id);

    List<TourSchedule> findByTour_TourId(Long tourId);
}
