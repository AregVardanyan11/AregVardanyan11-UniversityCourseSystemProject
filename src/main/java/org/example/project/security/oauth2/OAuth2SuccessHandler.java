package org.example.project.security.oauth2;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.project.model.User;
import org.example.project.repository.UserRepository;
import org.example.project.security.jwt.JwtUtil;
import org.example.project.security.jwt.UserPrincipal;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException, ServletException {

        OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();
        String email = oauth2User.getAttribute("email");

        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("User not found in system. Ask admin to register you.");
            return;
        }

        UserPrincipal principal = new UserPrincipal(userOpt.get());
        String jwt = jwtUtil.generateAccessToken(principal);
        String refreshToken = jwtUtil.generateRefreshToken(principal);

        response.setHeader("Authorization", "Bearer " + jwt);
        response.setHeader("Refresh-Token", refreshToken);
        response.setContentType("application/json");
        response.getWriter().write("""
                {
                    "accessToken": "%s",
                    "refreshToken": "%s"
                }
                """.formatted(jwt, refreshToken));
    }
}
