package vietravel.example.vietravel.Service.ServiceInterface;

import vietravel.example.vietravel.Model.AvailableDate;
import vietravel.example.vietravel.Model.Tour;
import vietravel.example.vietravel.dto.DestinationDto;
import vietravel.example.vietravel.dto.TourDto;
import vietravel.example.vietravel.dto.TourPlanDto;

import java.util.List;

public interface TourService {
    List<AvailableDate> generateAvailableDates();

    TourDto createTour(TourDto tourDto);

    TourDto updateTour(Long id, TourDto tourDto);

    void deleteTour(Long id);

    List<TourDto> getAllTour(String sortBy);

    TourDto getTourById(Long id);

    List<TourDto> getTrendingTours(int limit);
    List<TourPlanDto> getTourPlansByTourId(Long id);

    List<TourDto> searchTours(String destination, Integer days, String category,
                              Double minPrice, Double maxPrice, String sortBy);

}
