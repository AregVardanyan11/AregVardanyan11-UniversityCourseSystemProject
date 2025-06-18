package org.example.project.service;

import lombok.RequiredArgsConstructor;
import org.example.project.dto.request.CreateUserDto;
import org.example.project.dto.response.UserResponseDto;
import org.example.project.model.User;
import org.example.project.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public UserResponseDto addUser(CreateUserDto dto) {
        if (userRepository.existsByUsername(dto.getUsername())) {
            throw new IllegalArgumentException("Username already exists");
        }
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }

        User user = User.builder()
                .username(dto.getUsername())
                .password(dto.getPassword())
                .email(dto.getEmail())
                .build();

        User saved = userRepository.save(user);

        return UserResponseDto.builder()
                .id(saved.getId())
                .username(saved.getUsername())
                .email(saved.getEmail())
                .imagePath(saved.getImagePath())
                .build();
    }
}
