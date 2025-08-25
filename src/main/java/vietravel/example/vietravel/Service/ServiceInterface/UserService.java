package vietravel.example.vietravel.Service.ServiceInterface;

import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;
import vietravel.example.vietravel.dto.UserDto;
import java.util.List;

public interface UserService {
    UserDto createUser(UserDto dto, PasswordEncoder encoder);

    UserDto createGuideAccount(UserDto dto);

    void deleteUser(Long id);
    UserDto getUserById(Long id);
    List<UserDto> getAllUsers();
    UserDto getUserInfo(String email);
    UserDto updateUserProfileWithAvatar(Long id, UserDto dto, MultipartFile avatar);
}
