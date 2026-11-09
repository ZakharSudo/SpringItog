package com.example.bookingservice.controller;

import com.example.bookingservice.entity.User;
import com.example.bookingservice.repository.UserRepository;
import com.example.bookingservice.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public Map<String, String> register(@RequestBody Map<String, String> request) {
        String username = request.get("username");
        String password = request.get("password");

        if (userRepository.findByUsername(username).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User already exists");
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole("USER"); // По умолчанию создаем USER
        userRepository.save(user);

        String token = jwtService.generateToken(user.getUsername(), user.getRole(), user.getId());
        return Map.of("token", token);
    }

    @PostMapping("/auth")
    public Map<String, String> auth(@RequestBody Map<String, String> request) {
        User user = userRepository.findByUsername(request.get("username"))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials"));

        if (!passwordEncoder.matches(request.get("password"), user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
        }

        String token = jwtService.generateToken(user.getUsername(), user.getRole(), user.getId());
        return Map.of("token", token);
    }
}