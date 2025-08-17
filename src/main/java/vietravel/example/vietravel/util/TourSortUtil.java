package vietravel.example.vietravel.util;

import vietravel.example.vietravel.Model.Tour;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class TourSortUtil {

    public static List<Tour> sortTours(List<Tour> tours, String sortBy) {
        if (tours == null || tours.isEmpty()) {
            return List.of();
        }

        switch (sortBy.toLowerCase()) {
            case "top":
                tours.sort((t1, t2) -> Integer.compare(
                        t2.getTourSchedules() != null ? t2.getTourSchedules().size() : 0,
                        t1.getTourSchedules() != null ? t1.getTourSchedules().size() : 0
                ));
                break;

            case "lowest":
                tours.sort((t1, t2) -> Double.compare(
                        t1.getPrice() != null ? t1.getPrice() : 0.0,
                        t2.getPrice() != null ? t2.getPrice() : 0.0
                ));
                break;

            case "reviewed":
                // TODO: implement logic sort by reviews sau
                break;

            default:
                // sort theo id mặc định thay vì throw exception
                tours = tours.stream()
                        .sorted(Comparator.comparingLong(Tour::getTourId))
                        .collect(Collectors.toList());
                break;
        }

        return tours;
    }



}
