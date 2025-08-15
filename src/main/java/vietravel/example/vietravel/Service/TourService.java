package vietravel.example.vietravel.Service;

import vietravel.example.vietravel.dto.DestinationDto;
import vietravel.example.vietravel.dto.TourDto;
import vietravel.example.vietravel.dto.TourPlanDto;

import java.util.List;

public interface TourService {

    TourDto createTour(TourDto tourDto);

    TourDto updateTour(Long id, TourDto tourDto);

    void deleteTour(Long id);

    List<TourDto> getAllTour();

    TourDto getTourById(Long id);

    List<TourPlanDto> getTourPlansByTourId(Long id);
    List<TourDto> getAllToursSorted(String sortBy);


}
