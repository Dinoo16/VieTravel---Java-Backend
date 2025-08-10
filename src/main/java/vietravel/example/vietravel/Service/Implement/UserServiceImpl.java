package vietravel.example.vietravel.Service.Implement;


import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import vietravel.example.vietravel.Enum.UserRole;
import vietravel.example.vietravel.Model.User;
import vietravel.example.vietravel.Repository.UserRepository;
import vietravel.example.vietravel.Service.UserService;
import vietravel.example.vietravel.dto.UserDto;

import java.util.List;
import java.util.Optional;
import java.util.Random;
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
        dto.setRole(user.getRole());
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
                .role(dto.getRole())
                .phone(dto.getPhone())
                .avatar(dto.getAvatar())
                .build();
    }

    // Create normal user

    public Integer getRandomAvatar() {
        int number = new Random().nextInt(13) + 1;
        return number;
    }
    @Override
    public UserDto createUser(UserDto dto, PasswordEncoder encoder) {
        if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }

        User user = new User();
        user.setEmail(dto.getEmail());
        user.setPassword(encoder.encode(dto.getPassword()));
        user.setRole(UserRole.CUSTOMER); // default role
        user.setAvatar("avatar_" + (getRandomAvatar()));
        User saved = userRepository.save(user);
        return toDto(saved);
    }

    // Admin create guide
    public UserDto createGuideAccount(UserDto dto) {
//        if (userRepository.existsByEmail(email)) {
//            throw new RuntimeException("Email already in use");
//        }
        User user = new User();
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        user.setRole(UserRole.GUIDE);
        User saved = userRepository.save(user);
        return toDto(saved);
    }


    @Override
    public UserDto updateUserProfile(Long id, UserDto userDto) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isEmpty()) {
            throw new RuntimeException("User not found");
        }
        User user = optionalUser.get();
        // Update fields based on profile data
        user.setName(userDto.getName());
        user.setPhone(userDto.getPhone());
        user.setAvatar(userDto.getAvatar());
        User updatedUser = userRepository.save(user);
        return toDto(updatedUser);
    }

    @Override
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        userRepository.delete(user);
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

    @Override
    public UserDto getUserInfo(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
        return toDto(user);
    }


}
