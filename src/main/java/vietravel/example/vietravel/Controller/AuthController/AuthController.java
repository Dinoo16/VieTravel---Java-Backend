package vietravel.example.vietravel.Controller.AuthController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import vietravel.example.vietravel.Config.JwtAuthenticationFilter;
import vietravel.example.vietravel.Config.JwtUtil;
import vietravel.example.vietravel.Enum.UserRole;
import vietravel.example.vietravel.Model.User;
import vietravel.example.vietravel.Repository.UserRepository;
import vietravel.example.vietravel.Service.Implement.UserServiceImpl;
import vietravel.example.vietravel.Service.UserService;
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

        UserDto user = userService.createUser(userDto, passwordEncoder);
        String token = jwtUtil.generateToken(user.getEmail(), user.getRole().name());
//        return ResponseEntity.status(HttpStatus.CREATED).body(user);
        return ResponseEntity.ok(Collections.singletonMap("token", token));
    }


    // ✅ (Optional) Dummy signin logic — actual logic should verify password and return token
    @PostMapping("/signin")
    public ResponseEntity<?> signin(@RequestBody UserDto userDto) {
        return userRepository.findByEmail(userDto.getEmail())
                .map(user -> {
                    if (passwordEncoder.matches(userDto.getPassword(), user.getPassword())) {
                        String token = jwtUtil.generateToken(user.getEmail(), user.getRole().name());
                        return ResponseEntity.ok(Collections.singletonMap("token", token));
                        // Return JWT or session token here if implemented
                    } else {
                        return ResponseEntity.status(401).body("Invalid password");
                    }
                })
                .orElse(ResponseEntity.status(404).body("User not found"));
    }
}
