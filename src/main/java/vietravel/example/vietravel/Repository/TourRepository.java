package vietravel.example.vietravel.Repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vietravel.example.vietravel.Model.Tour;
import vietravel.example.vietravel.Model.TourPlan;

import java.util.List;
import java.util.Optional;

@Repository
public interface TourRepository extends JpaRepository<Tour, Long> {
    // Find tours by name containing a keyword (for search)
//    List<Tour> findByNameContainingIgnoreCase(String keyword);
    List<Tour> findAll(Sort sort);

    @Query("SELECT t FROM Tour t " +
            "WHERE (:destination IS NULL OR t.destination.name LIKE %:destination%) " +
            "AND (:days IS NULL OR t.duration = :days)" +
            "AND (:category IS NULL OR EXISTS (SELECT c FROM t.categories c WHERE c.name = :category)) " +
            "AND (:minPrice IS NULL OR t.price >= :minPrice) " +
            "AND (:maxPrice IS NULL OR t.price <= :maxPrice) "
           )
    List<Tour> searchTours(@Param("destination") String destination,
                           @Param("days") Integer days,
                           @Param("category") String category,
                           @Param("minPrice") Double minPrice,
                           @Param("maxPrice") Double maxPrice )
                           ;




}
