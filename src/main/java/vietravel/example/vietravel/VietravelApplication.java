package vietravel.example.vietravel;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.password.PasswordEncoder;
import vietravel.example.vietravel.Enum.UserRole;
import vietravel.example.vietravel.Model.User;
import vietravel.example.vietravel.Repository.UserRepository;

@SpringBootApplication
@EnableScheduling
public class VietravelApplication {

	public static void main(String[] args) {

		SpringApplication.run(VietravelApplication.class, args);
	}

	@Bean
	CommandLineRunner initAdmin(UserRepository userRepository, PasswordEncoder encoder) {
		return args -> {
			userRepository.findByEmail("admin@domain.com").orElseGet(() -> {
				User admin = User.builder()
						.email("admin@domain.com")
						.name("Admin")
						.password(encoder.encode("admin123"))
						.role(UserRole.ADMIN)
						.build();
				return userRepository.save(admin);
			});
		};
	}


}
