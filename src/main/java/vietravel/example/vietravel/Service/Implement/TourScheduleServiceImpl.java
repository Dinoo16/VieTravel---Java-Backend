package vietravel.example.vietravel.Service.Implement;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vietravel.example.vietravel.Enum.BookingStatus;
import vietravel.example.vietravel.Model.Booking;
import vietravel.example.vietravel.Model.Guide;
import vietravel.example.vietravel.Model.Tour;
import vietravel.example.vietravel.Model.TourSchedule;
import vietravel.example.vietravel.Repository.BookingRepository;
import vietravel.example.vietravel.Repository.GuideRepository;
import vietravel.example.vietravel.Repository.TourRepository;
import vietravel.example.vietravel.Repository.TourScheduleRepository;
import vietravel.example.vietravel.Service.ServiceInterface.TourScheduleService;
import vietravel.example.vietravel.dto.BookingSummaryDto;
import vietravel.example.vietravel.dto.TourScheduleDetailDto;
import vietravel.example.vietravel.dto.TourScheduleDto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    public TourScheduleDto createDynamicSchedule(Long tourId, LocalDate departureDate, LocalTime departureTime, List<Long> guideIds, List<Long> bookingIds) {
        Tour tour = tourRepository.findById(tourId)
                .orElseThrow(() -> new RuntimeException("Tour not found"));

        LocalDate returnTime = departureDate.plusDays(tour.getDuration());

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

    // Admin Get schedule by id
    @Override
    public TourScheduleDto getScheduleById(Long id) {
        return scheduleRepository.findById(id)
                .map(this::toDto)
                .orElseThrow(() -> new RuntimeException("Schedule not found"));
    }

    // Admin get all schedule
    @Override
    public List<TourScheduleDto> getAllSchedules() {
        return scheduleRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());

    }

    // User get all schedules (upcoming, done, cancel)
    public Map<String, List<TourScheduleDetailDto>> getUserSchedules(Long userId) {
        List<TourSchedule> schedules = scheduleRepository.findByBookings_User_UserId(userId);

        LocalDate now = LocalDate.now();

        List<TourScheduleDetailDto> upcoming = schedules.stream()
                .filter(s -> s.getDepartureDate().isAfter(now))
                .filter(s -> s.getBookings().stream().anyMatch(b -> b.getUser().getUserId().equals(userId) && b.getStatus() == BookingStatus.PAID))
                .map(s -> toDetailDto(s, userId))
                .toList();
        List<TourScheduleDetailDto> onGoing = schedules.stream()
                .filter(s -> !s.getDepartureDate().isAfter(now)) // departureDate <= now
                .filter(s -> !s.getReturnTime().isBefore(now))   // returnTime >= now
                .filter(s -> s.getBookings().stream()
                        .anyMatch(b -> b.getUser().getUserId().equals(userId)
                                && b.getStatus() == BookingStatus.PAID))
                .map(s -> toDetailDto(s, userId))
                .toList();

        List<TourScheduleDetailDto> done = schedules.stream()
                .filter(s -> s.getReturnTime().isBefore(now))
                .filter(s -> s.getBookings().stream()
                        .anyMatch(b -> b.getUser().getUserId().equals(userId)
                                && b.getStatus() == BookingStatus.PAID))
                .map(s -> toDetailDto(s, userId))
                .toList();


        Map<String, List<TourScheduleDetailDto>> result = new HashMap<>();

        result.put("upcoming", upcoming);
        result.put("ongoing", onGoing);
        result.put("done", done);


        return result;

    }


    private TourScheduleDto toDto(TourSchedule schedule, Long userId) {
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
        // Filter booking
        List<Long> bookingIds = schedule.getBookings().stream()
                .filter(b -> b.getUser().getUserId().equals(userId))
                .map(Booking::getBookingId)
                .toList();
        dto.setBookingIds(bookingIds);
        return dto;
    }

    private TourScheduleDetailDto toDetailDto(TourSchedule schedule, Long userId) {
        TourScheduleDetailDto dto = new TourScheduleDetailDto();
        Tour tour = schedule.getTour();

        dto.setId(schedule.getId());
        dto.setTourId(schedule.getTour().getTourId());
        dto.setDepartureDate(schedule.getDepartureDate());
        dto.setDepartureTime(schedule.getDepartureTime());
        dto.setReturnTime(schedule.getReturnTime());

        dto.setTitle(tour.getTitle());
        dto.setBackgroundImage(tour.getBackgroundImage());
        dto.setDestinationName(tour.getDestination().getName());
        dto.setDuration(tour.getDuration());

        List<BookingSummaryDto> summaries = schedule.getBookings().stream()
                .filter(b -> b.getUser().getUserId().equals(userId))
                .map(b -> new BookingSummaryDto(b.getBookingId(), b.getNumberOfPeople(), b.getStatus()))
                .toList();

        dto.setBookingSummaries(summaries);

        dto.setGuideNames(
                schedule.getGuides().stream()
                        .map(Guide::getName)
                        .collect(Collectors.toList())
        );
        return dto;

    }

    // Admin
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
        List<Long> bookingIds = schedule.getBookings().stream()
                .map(Booking::getBookingId)
                .toList();
        dto.setBookingIds(bookingIds);
        return dto;
    }

}

