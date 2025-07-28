package vietravel.example.vietravel.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vietravel.example.vietravel.Model.Destination;

import java.util.Optional;

@Repository
public interface DestinationRepository extends JpaRepository<Destination, Long> {

    // Find by name
    Optional<Destination> findByName(String destinationName);


}
