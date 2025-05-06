package com.backend.service;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.backend.dto.LoginDto;
import com.backend.dto.LoginResponseDto;
import com.backend.dto.RegisterDto;
import com.backend.entity.User;
import com.backend.repository.UserRepository;
import com.backend.security.JwtUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository  userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil         jwtUtil;

    public User register(RegisterDto registerDto) {
        if (userRepository.findByUsername(registerDto.getUsername()).isPresent()) {
            throw new RuntimeException("Username already exist");
        }
        User user = User.builder()
        .password(passwordEncoder.encode(registerDto.getPassword()))
        .username(registerDto.getUsername()).build();
        return userRepository.save(user);
    };

    public User login(LoginDto loginDto) {
        Optional<User> optionalUser = userRepository.findByUsername(loginDto.getUsername());
        if (!optionalUser.isPresent()) {
            throw new RuntimeException("Invalid username");
        }
        User user = optionalUser.get();
        if (!passwordEncoder.matches(loginDto.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }
        return user;
    };

    public LoginResponseDto loginResponseDto (LoginDto loginDto) {
        Optional<User> optionalUser = userRepository.findByUsername(loginDto.getUsername());
        if (!optionalUser.isPresent()) {
            throw new RuntimeException("Invalid username");
        }
        User user = optionalUser.get();
        if (!loginDto.getPassword().matches(user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }
        String token = jwtUtil.generateToken(loginDto.getUsername());
        return new LoginResponseDto(token);
    };
}
