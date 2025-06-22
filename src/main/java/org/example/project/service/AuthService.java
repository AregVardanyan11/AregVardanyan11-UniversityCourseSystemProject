package org.example.project.service;

import lombok.RequiredArgsConstructor;
import org.example.project.model.User;
import org.example.project.security.jwt.JwtUtil;
import org.example.project.security.jwt.UserPrincipal;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    public User getUserFromHearer(String authHeader){
        String token = authHeader.startsWith("Bearer ") ? authHeader.substring(7) : authHeader;
        String username = jwtUtil.getUsername(token);
        UserPrincipal principal = (UserPrincipal) userDetailsService.loadUserByUsername(username);
        return principal.getUser();
    }
}
