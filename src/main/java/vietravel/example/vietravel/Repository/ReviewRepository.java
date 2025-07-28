package vietravel.example.vietravel.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vietravel.example.vietravel.Model.Review;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    // Find review by tour id
    List<Review> findByTourId(Long tourId);

    // Check if exist any reviews by user id and tour id
    boolean existsByUserIdAndTourId(Long userId, Long tourId);
}
