package vietravel.example.vietravel.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vietravel.example.vietravel.Model.Booking;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByTourId(Long tourId);
    List<Booking> findByUserUserId(Long userId);
    List<Booking> findByTourScheduleId(Long tourScheduleId);
}
