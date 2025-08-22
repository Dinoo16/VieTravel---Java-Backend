package vietravel.example.vietravel.Controller.AuthController;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import vietravel.example.vietravel.Config.JwtUtil;
import vietravel.example.vietravel.Enum.UserRole;
import vietravel.example.vietravel.Repository.UserRepository;
import vietravel.example.vietravel.Service.ServiceInterface.UserService;
import vietravel.example.vietravel.dto.UserDto;
import org.springframework.validation.BindingResult;

import java.util.Collections;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserService userService;

    // ✅ Optional: health check or basic message
    @GetMapping("/ping")
    public ResponseEntity<String> ping() {
        return ResponseEntity.ok("Auth API is running");
    }

    // ✅ Signup
    @PostMapping("/signup")
    public ResponseEntity<?> signupHandle(@Valid @RequestBody UserDto userDto, BindingResult result) {
        if (result.hasErrors()) {
            // Trả về danh sách lỗi nếu có
            return ResponseEntity.badRequest().body(
                    result.getFieldErrors().stream()
                            .map(e -> e.getField() + ": " + e.getDefaultMessage())
                            .toList()
            );
        }
        if (userDto.getRole() == null) {
            userDto.setRole(UserRole.USER);
        }

        UserDto user = userService.createUser(userDto, passwordEncoder);

        String token = jwtUtil.generateToken(user.getEmail(), user.getRole().name());
//        return ResponseEntity.status(HttpStatus.CREATED).body(user);
        return ResponseEntity.ok(Collections.singletonMap("token", token));
    }


    @PostMapping("/signin")
    public ResponseEntity<?> signin(@RequestBody UserDto userDto) {
        try {
            return userRepository.findByEmail(userDto.getEmail())
                    .map(user -> {
                        if (passwordEncoder.matches(userDto.getPassword(), user.getPassword())) {
                            String token = jwtUtil.generateToken(user.getEmail(), user.getRole().name());
                            return ResponseEntity.ok(Collections.singletonMap("token", token));
                        } else {
                            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid password");
                        }
                    })
                    .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found"));
        } catch (Exception e) {
            e.printStackTrace(); // logs to console
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred");
        }
    }

    @GetMapping("/me")
    public ResponseEntity<?> getUserInfo(Authentication authentication) {
        String email = authentication.getName(); // get email from JWT token
        UserDto userDto = userService.getUserInfo(email);
        return ResponseEntity.ok(userDto);
    }


}
