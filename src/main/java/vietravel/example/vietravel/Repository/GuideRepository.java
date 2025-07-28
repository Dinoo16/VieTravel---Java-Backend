package vietravel.example.vietravel.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vietravel.example.vietravel.Model.Guide;

import java.util.Optional;

@Repository
public interface GuideRepository extends JpaRepository<Guide, Long> {

    // find guide by user id
    Optional<Guide> findByUserId(Long userId);
    //Find by name
    Optional<Guide> findByName(String name);
}
