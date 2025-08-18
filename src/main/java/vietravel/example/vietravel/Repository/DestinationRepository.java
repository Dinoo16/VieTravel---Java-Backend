package vietravel.example.vietravel.Repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vietravel.example.vietravel.Model.Destination;
import vietravel.example.vietravel.dto.DestinationDto;

import java.util.List;
import java.util.Optional;

@Repository
public interface DestinationRepository extends JpaRepository<Destination, Long> {

    // Find by name
    Optional<Destination> findByName(String destinationName);

    boolean existsByNameIgnoreCase(String destinationName);

    @Query("SELECT d FROM Destination d LEFT JOIN d.tours t " +
            "GROUP BY d " +
            "ORDER BY COUNT(t) DESC")
    List<Destination> findPopularDestinations(Pageable pageable);

}
