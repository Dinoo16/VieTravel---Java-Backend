package vietravel.example.vietravel.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import vietravel.example.vietravel.Model.AvailableDate;
import vietravel.example.vietravel.Model.Tour;
import vietravel.example.vietravel.Repository.TourRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class TourScheduler {

    @Autowired
    private TourRepository tourRepository;

    @Scheduled(cron = "0 0 0 * * *")
    public void refreshAvailableDates() {
        List<Tour> tours = tourRepository.findAll();
        LocalDateTime today = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);

        for (Tour tour : tours) {
            List<AvailableDate> updatedDates = new ArrayList<>();

            for (int i = 0; i < 30; i++) {
                LocalDateTime date = today.plusDays(i);

                // check nếu đã tồn tại trong list
                boolean exists = tour.getAvailableDates().stream()
                        .anyMatch(d -> d.getDate().toLocalDate().equals(date.toLocalDate()));

                if (!exists) {
                    updatedDates.add(new AvailableDate(date, true));
                }
            }

            tour.getAvailableDates().addAll(updatedDates);
            tourRepository.save(tour);
        }
    }
}

