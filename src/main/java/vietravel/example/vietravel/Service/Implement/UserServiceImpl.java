package vietravel.example.vietravel.Service.Implement;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vietravel.example.vietravel.Enum.UserRole;
import vietravel.example.vietravel.Model.User;
import vietravel.example.vietravel.Repository.UserRepository;
import vietravel.example.vietravel.Service.UserService;
import vietravel.example.vietravel.dto.UserDto;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private UserDto toDto(User user) {
        UserDto dto = new UserDto();
        dto.setId(user.getUserId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setPassword(user.getPassword());
        dto.setRole(user.getRole().name());
        dto.setPhone(user.getPhone());
        dto.setAvatar(user.getAvatar());
        return dto;
    }

    private User toEntity(UserDto dto) {
        return User.builder()
                .userId(dto.getId())
                .name(dto.getName())
                .email(dto.getEmail())
                .password(dto.getPassword())
                .role(UserRole.valueOf(dto.getRole().toUpperCase()))
                .phone(dto.getPhone())
                .avatar(dto.getAvatar())
                .build();
    }

    @Override
    public UserDto createUser(UserDto dto) {
        User user = toEntity(dto);
        return toDto(userRepository.save(user));
    }

    @Override
    public UserDto updateUser(Long id, UserDto dto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        user.setRole(UserRole.valueOf(dto.getRole().toUpperCase()));
        user.setPhone(dto.getPhone());
        user.setAvatar(dto.getAvatar());
        return toDto(userRepository.save(user));
    }

    @Override
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not found");
        }
        userRepository.deleteById(id);
    }

    @Override
    public UserDto getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return toDto(user);
    }

    @Override
    public List<UserDto> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}
