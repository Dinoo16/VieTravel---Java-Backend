package vietravel.example.vietravel.Service;

import org.springframework.security.crypto.password.PasswordEncoder;
import vietravel.example.vietravel.dto.UserDto;
import java.util.List;

public interface UserService {
    UserDto createUser(UserDto dto, PasswordEncoder encoder);

    UserDto createGuideAccount(UserDto dto);
    UserDto updateUserProfile(Long id, UserDto dto);
//    UserDto changePassword(Long id, UserDto dto);

    void deleteUser(Long id);
    UserDto getUserById(Long id);
    List<UserDto> getAllUsers();
}
