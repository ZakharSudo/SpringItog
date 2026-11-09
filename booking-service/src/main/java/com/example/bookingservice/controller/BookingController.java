package com.example.bookingservice.controller;

import com.example.bookingservice.dto.CreateBookingRequest;
import com.example.bookingservice.entity.Booking;
import com.example.bookingservice.service.BookingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bookings") // <--- Именно этот адрес
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public Booking createBooking(@AuthenticationPrincipal Jwt jwt, @Valid @RequestBody CreateBookingRequest request) {
        Long userId = jwt.getClaim("userId");
        return bookingService.createBooking(userId, request);
    }

    // ... (тут могут быть другие методы) ...
}