package vietravel.example.vietravel.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vietravel.example.vietravel.Model.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Find user by email
    Optional<User> findByEmail(String email);

    // Check if user with email exists (for validation)
    boolean existsByEmail(String email);

    // Find user by username (if you have it)
    Optional<User> findByUserName(String username);

    // Check if user with phone number exists
    boolean existByPhone(String phone);
}
