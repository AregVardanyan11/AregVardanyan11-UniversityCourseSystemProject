package org.example.project.service;

import lombok.RequiredArgsConstructor;
import org.example.project.dto.criteria.UserSearchCriteria;
import org.example.project.dto.request.LoginRequest;
import org.example.project.dto.request.CreateUserDto;
import org.example.project.dto.request.RegisterRequest;
import org.example.project.dto.request.UpdateUserDto;
import org.example.project.dto.response.AuthResponseDto;
import org.example.project.dto.response.UserResponseDto;
import org.example.project.model.User;
import org.example.project.model.enums.UserRole;
import org.example.project.repository.UserRepository;
import org.example.project.security.jwt.JwtUtil;
import org.example.project.security.jwt.UserPrincipal;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public UserResponseDto addUser(CreateUserDto dto) {
        if (userRepository.existsByEmailOrUsername(dto.getEmail(), dto.getUsername())) {
            throw new IllegalArgumentException("User with this email or username already exists");
        }

        User user = User.builder()
                .username(dto.getUsername())
                .email(dto.getEmail())
                .password(dto.getPassword())
                .build();

        return map(userRepository.save(user));
    }

    public List<UserResponseDto> getAll() {
        return userRepository.findAll()
                .stream()
                .map(this::map)
                .collect(Collectors.toList());
    }

    public UserResponseDto getById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        return map(user);
    }

    @Transactional
    public UserResponseDto updateUser(Long id, UpdateUserDto dto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Optional<User> conflict = userRepository.findByUsernameOrEmail(dto.getUsername(), dto.getEmail());
        if (conflict.isPresent() && !conflict.get().getId().equals(id)) {
            throw new IllegalArgumentException("Another user already has this username or email");
        }

        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());

        return map(userRepository.save(user));
    }

    @Transactional
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new IllegalArgumentException("User not found");
        }
        userRepository.deleteById(id);
    }

    public List<UserResponseDto> search(UserSearchCriteria criteria, PageRequest pageRequest) {
        return userRepository.search(criteria, pageRequest)
                .stream()
                .map(this::map)
                .collect(Collectors.toList());
    }

    private UserResponseDto map(User user) {
        return UserResponseDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .imagePath(user.getImagePath())
                .build();
    }


    public AuthResponseDto login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow();

        UserPrincipal principal = new UserPrincipal(user); // convert

        return AuthResponseDto.builder()
                .accessToken(jwtUtil.generateAccessToken(principal))
                .refreshToken(jwtUtil.generateRefreshToken(principal))
                .role(user.getRole().name())
                .build();
    }

    public void register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("Username already exists");
        }

        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(UserRole.valueOf(request.getRole()))
                .build();

        userRepository.save(user);
    }

    public String refresh(String refreshToken) {
        if (!jwtUtil.isVerified(refreshToken)) {
            throw new IllegalArgumentException("Invalid token");
        }

        String username = jwtUtil.getUsername(refreshToken);
        User user = userRepository.findByUsername(username).orElseThrow();
        return jwtUtil.generateAccessToken(new UserPrincipal(user));
    }

    public boolean existsAdmin() {
        return userRepository.existsByRole(UserRole.ROLE_ADMIN);
    }
}
