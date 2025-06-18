package org.example.project.repository;

import jakarta.validation.constraints.NotBlank;
import org.example.project.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);

    boolean existsByEmailOrUsername(String email, String username);

    boolean existsByUsername(@NotBlank String username);
}
