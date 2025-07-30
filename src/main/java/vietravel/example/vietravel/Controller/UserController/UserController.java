package vietravel.example.vietravel.Controller.UserController;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vietravel.example.vietravel.Service.UserService;
import vietravel.example.vietravel.dto.UserDto;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@PreAuthorize("hasRole('USER') or hasRole('GUIDE')")
public class UserController {

    private final UserService userService;

    // Create user
    @PostMapping
    public ResponseEntity<UserDto> createUser(@RequestBody UserDto userDto) {
        UserDto user = userService.createUser(userDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }


    // Update user's profile (name, phone, avatar)
    @PutMapping("/{id}/profile")
    public ResponseEntity<UserDto> updateUserProfile(@PathVariable Long id, @RequestBody UserDto userDto) {
        UserDto user = userService.updateUserProfile(id, userDto);
        return ResponseEntity.ok(user);
    }

    // Get user by ID
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        UserDto user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }
}
