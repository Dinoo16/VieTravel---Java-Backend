package vietravel.example.vietravel.Service;


import vietravel.example.vietravel.Model.Destination;
import vietravel.example.vietravel.dto.DestinationDto;

import java.util.List;

public interface DestinationService {

    DestinationDto createDestination(DestinationDto destinationDto);

    DestinationDto updateDestination(Long id, DestinationDto destinationDto);

    void deleteDestination(Long id);

    List<DestinationDto> getAllDestinations();

    DestinationDto getDestinationById(Long id);


}
