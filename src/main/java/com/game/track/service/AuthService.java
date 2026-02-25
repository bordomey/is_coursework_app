package com.game.track.service;

import com.game.track.config.JwtUtil;
import com.game.track.dto.LoginRequest;
import com.game.track.dto.RegisterRequest;
import com.game.track.dto.UserDto;
import com.game.track.entity.User;
import com.game.track.exception.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;
    
    @Autowired
    private JwtUtil jwtUtil;

    public UserDto register(RegisterRequest request) {
        return userService.registerUser(request);
    }

    public String login(LoginRequest request) {

            User user = userService.findUserByEmail(request.getEmail());
            
            if (user == null) {
                throw new ValidationException("Invalid credentials");
            }

            if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
                throw new ValidationException("Invalid credentials");
            }
            
            org.springframework.security.core.userdetails.User userDetails = 
                new org.springframework.security.core.userdetails.User(
                    user.getEmail(), 
                    user.getPasswordHash(), 
                    java.util.Collections.emptyList()
                );
            
            UsernamePasswordAuthenticationToken authToken = 
                new UsernamePasswordAuthenticationToken(
                    userDetails, 
                    null, 
                    java.util.Collections.emptyList()
                );
            
            SecurityContextHolder.getContext().setAuthentication(authToken);
            
            String token = jwtUtil.generateToken(userDetails);
            return token;

    }
}