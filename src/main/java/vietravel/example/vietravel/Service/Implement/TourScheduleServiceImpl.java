package vietravel.example.vietravel.Service.Implement;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vietravel.example.vietravel.Model.Booking;
import vietravel.example.vietravel.Model.Guide;
import vietravel.example.vietravel.Model.Tour;
import vietravel.example.vietravel.Model.TourSchedule;
import vietravel.example.vietravel.Repository.BookingRepository;
import vietravel.example.vietravel.Repository.GuideRepository;
import vietravel.example.vietravel.Repository.TourRepository;
import vietravel.example.vietravel.Repository.TourScheduleRepository;
import vietravel.example.vietravel.Service.ServiceInterface.TourScheduleService;
import vietravel.example.vietravel.dto.TourScheduleDto;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TourScheduleServiceImpl implements TourScheduleService {

    private final TourScheduleRepository scheduleRepository;
    private final TourRepository tourRepository;
    private final GuideRepository guideRepository;

    private final BookingRepository bookingRepository;


    // Use when creating a custom schedule with specific date and guides (not sample)
    @Override
    public TourScheduleDto createDynamicSchedule(Long tourId, LocalDateTime departureDate, LocalTime departureTime, List<Long> guideIds, List<Long> bookingIds) {
        Tour tour = tourRepository.findById(tourId)
                .orElseThrow(() -> new RuntimeException("Tour not found"));

        LocalDateTime returnTime = departureDate.plusDays(tour.getDuration());

        List<Guide> guides = guideRepository.findAllById(guideIds);


        List<Booking> validBookings = bookingRepository.findAllById(bookingIds).stream()
                .filter(b -> "PAID".equals(b.getStatus()))
                .collect(Collectors.toList());

        if(validBookings.isEmpty()) {
            throw new RuntimeException("Cannot create TourSchedule without at least one PAID booking");
        }

        TourSchedule schedule = TourSchedule.builder()
                .tour(tour)
                .departureDate(departureDate)
                .departureTime(departureTime)
                .returnTime(returnTime)
                .guides(guides)
                .bookings(validBookings)
                .build();

        return toDto(scheduleRepository.save(schedule));
    }

    // Get schedule by id
    @Override
    public TourScheduleDto getScheduleById(Long id) {
        return scheduleRepository.findById(id)
                .map(this::toDto)
                .orElseThrow(() -> new RuntimeException("Schedule not found"));
    }

    // Get all schedule
    @Override
    public List<TourScheduleDto> getAllSchedules() {
        return scheduleRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());

    }

    private TourScheduleDto toDto(TourSchedule schedule) {
        TourScheduleDto dto = new TourScheduleDto();
        dto.setId(schedule.getId());
        dto.setTourId(schedule.getTour().getTourId());
        dto.setDepartureDate(schedule.getDepartureDate());
        dto.setDepartureTime(schedule.getDepartureTime());
        dto.setReturnTime(schedule.getReturnTime());
        dto.setGuideIds(
                schedule.getGuides().stream()
                        .map(Guide::getId)
                        .collect(Collectors.toList())
        );
        dto.setBookingIds(
                schedule.getBookings().stream()
                        .map(Booking::getBookingId)
                        .collect(Collectors.toList())
        );
        return dto;
    }

}

