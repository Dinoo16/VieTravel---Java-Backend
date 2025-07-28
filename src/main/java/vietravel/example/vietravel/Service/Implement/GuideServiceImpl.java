package vietravel.example.vietravel.Service.Implement;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vietravel.example.vietravel.Enum.ExperienceLevel;
import vietravel.example.vietravel.Enum.JobStatus;
import vietravel.example.vietravel.Model.Guide;
import vietravel.example.vietravel.Model.TourSchedule;
import vietravel.example.vietravel.Model.User;
import vietravel.example.vietravel.Repository.GuideRepository;
import vietravel.example.vietravel.Repository.TourScheduleRepository;
import vietravel.example.vietravel.Repository.UserRepository;
import vietravel.example.vietravel.Service.GuideService;
import vietravel.example.vietravel.dto.GuideDto;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GuideServiceImpl implements GuideService {

    private final GuideRepository guideRepository;
    private final UserRepository userRepository;
    private final TourScheduleRepository tourScheduleRepository;

    private GuideDto toDto(Guide guide) {
        GuideDto dto = new GuideDto();
        dto.setId(guide.getId());
        dto.setUserId(guide.getUser().getUserId());
        dto.setName(guide.getName());
        dto.setEmail(guide.getEmail());
        dto.setPhoneNo(guide.getPhoneNo());
        dto.setBio(guide.getBio());
        dto.setAvatar(guide.getAvatar());
        dto.setRole(guide.getRole());
        dto.setExperienceLevel(guide.getExperienceLevel().name());
        dto.setJobAchievement(guide.getJobAchievement());
        dto.setJobStatus(guide.getJobStatus().name());
        dto.setWorkExperience(guide.getWorkExperience());

        if (guide.getTourSchedules() != null) {
            dto.setTourScheduleIds(
                    guide.getTourSchedules().stream()
                            .map(TourSchedule::getId)
                            .collect(Collectors.toList())
            );
        }

        return dto;
    }

    private Guide toEntity(GuideDto dto) {
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<TourSchedule> schedules = dto.getTourScheduleIds() != null
                ? tourScheduleRepository.findAllById(dto.getTourScheduleIds())
                : List.of();

        return Guide.builder()
                .user(user)
                .name(dto.getName())
                .email(dto.getEmail())
                .phoneNo(dto.getPhoneNo())
                .bio(dto.getBio())
                .avatar(dto.getAvatar())
                .role(dto.getRole())
                .experienceLevel(ExperienceLevel.valueOf(dto.getExperienceLevel()))
                .jobAchievement(dto.getJobAchievement())
                .jobStatus(JobStatus.valueOf(dto.getJobStatus()))
                .workExperience(dto.getWorkExperience())
                .tourSchedules(schedules)
                .build();
    }

    @Override
    public GuideDto createGuide(GuideDto dto) {
        Guide guide = toEntity(dto);
        return toDto(guideRepository.save(guide));
    }

    @Override
    public GuideDto updateGuide(Long id, GuideDto dto) {
        Guide guide = guideRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Guide not found"));

        guide.setName(dto.getName());
        guide.setEmail(dto.getEmail());
        guide.setPhoneNo(dto.getPhoneNo());
        guide.setBio(dto.getBio());
        guide.setAvatar(dto.getAvatar());
        guide.setRole(dto.getRole());
        guide.setExperienceLevel(ExperienceLevel.valueOf(dto.getExperienceLevel()));
        guide.setJobAchievement(dto.getJobAchievement());
        guide.setJobStatus(JobStatus.valueOf(dto.getJobStatus()));
        guide.setWorkExperience(dto.getWorkExperience());

        if (dto.getTourScheduleIds() != null) {
            List<TourSchedule> schedules = tourScheduleRepository.findAllById(dto.getTourScheduleIds());
            guide.setTourSchedules(schedules);
        }

        return toDto(guideRepository.save(guide));
    }

    @Override
    public void deleteGuide(Long id) {
        if (!guideRepository.existsById(id)) {
            throw new RuntimeException("Guide not found");
        }
        guideRepository.deleteById(id);
    }

    @Override
    public GuideDto getGuideById(Long id) {
        Guide guide = guideRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Guide not found"));
        return toDto(guide);
    }

    @Override
    public List<GuideDto> getAllGuides() {
        return guideRepository.findAll()
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}
