package vietravel.example.vietravel.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vietravel.example.vietravel.Model.Tour;

import java.util.List;
import java.util.Optional;

@Repository
public interface TourRepository extends JpaRepository<Tour, Long> {

    // Find tours by id
    Optional<Tour> findById(Long id);

    // Find tours by name containing a keyword (for search)
    List<Tour> findByNameContainingIgnoreCase(String keyword);

}
