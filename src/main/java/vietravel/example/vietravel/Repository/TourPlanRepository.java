package vietravel.example.vietravel.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vietravel.example.vietravel.Model.Tour;
import vietravel.example.vietravel.Model.TourPlan;

import java.util.List;

@Repository
public interface TourPlanRepository extends JpaRepository<TourPlan, Long> {

    // Find by tour id
    List<TourPlan> findByTour_TourId(Long tourId);

    boolean existsByTourAndDay(Tour tour, int day);


}
