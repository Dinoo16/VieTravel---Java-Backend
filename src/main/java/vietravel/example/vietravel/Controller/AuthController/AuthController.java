package vietravel.example.vietravel.Controller.AuthController;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import vietravel.example.vietravel.Enum.UserRole;
import vietravel.example.vietravel.Model.User;
import vietravel.example.vietravel.Repository.UserRepository;
import vietravel.example.vietravel.dto.UserDto;

@Controller
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @GetMapping("/signin")
    public String getSignin(@RequestParam(value = "success", required = false) String success, Model model) {
        if (success != null) {
            model.addAttribute("message", "Registration successful! Please sign in.");
        }
        return "signin";
    }

    @GetMapping("/signup")
    public String getSignup(Model model) {
        model.addAttribute("user", new UserDto());
        return "signup";
    }

    @PostMapping("/signup")
    public String signupHandle(Model model, @Valid @ModelAttribute UserDto ut, BindingResult result) {

        if (result.hasErrors()) {
            System.out.println("❌ Validation failed: " + result.getAllErrors());
            model.addAttribute("user", ut);
            return "signup";
        }

        try {

            if (userRepository.findByEmail(ut.getEmail()).isPresent()) {
                System.out.println("❌ Email already exists!");
                model.addAttribute("error", "Email already exists!");
                return "signup";
            }

            // Default Role USER
            if (ut.getRole() == null) {
                ut.setRole(UserRole.CUSTOMER);
            }

            System.out.println("✅ Saving user: " + ut.getEmail());

            User newUser = new User(ut, passwordEncoder);
            userRepository.save(newUser);

            System.out.println("🎉 User saved successfully!");

            return "redirect:/auth/signin";

        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Error saving user: " + e.getMessage());
            return "signup";
        }
    }


}

