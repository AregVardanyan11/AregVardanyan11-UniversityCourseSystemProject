package org.example.project.repository;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.example.project.dto.criteria.UserSearchCriteria;
import org.example.project.model.User;
import org.example.project.model.enums.UserRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);

    boolean existsByEmailOrUsername(String email, String username);

    boolean existsByUsername(@NotBlank String username);
    
    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    @Query("""
        SELECT u FROM User u
        WHERE (:#{#criteria.username} IS NULL OR LOWER(u.username) LIKE LOWER(CONCAT('%', :#{#criteria.username}, '%')))
          AND (:#{#criteria.email} IS NULL OR LOWER(u.email) LIKE LOWER(CONCAT('%', :#{#criteria.email}, '%')))
    """)
    Page<User> search(UserSearchCriteria criteria, Pageable pageable);

    Optional<User> findByUsernameOrEmail(@NotBlank String username, @Email @NotBlank String email);

    boolean existsByRole(UserRole role);

    void removeByUsername(String username);
}
