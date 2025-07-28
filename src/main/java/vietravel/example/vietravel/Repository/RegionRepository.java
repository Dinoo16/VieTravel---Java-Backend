package vietravel.example.vietravel.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vietravel.example.vietravel.Model.Region;

import java.util.List;

@Repository
public interface RegionRepository extends JpaRepository<Region, Long> {

    // Find regions by name (case-insensitive partial match)
    List<Region> findByNameContainingIgnoreCase(String keyword);

    // Check if a region with a given name exists
    boolean existsByName(String name);

}
