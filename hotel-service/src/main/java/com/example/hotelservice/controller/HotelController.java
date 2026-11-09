package com.example.hotelservice.controller;

import com.example.hotelservice.entity.Hotel;
import com.example.hotelservice.repository.HotelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/hotels")
@RequiredArgsConstructor
public class HotelController {

    private final HotelRepository hotelRepository;

    // Публичный просмотр отелей
    @GetMapping
    public List<Hotel> getAllHotels() {
        return hotelRepository.findAll();
    }

    // СОЗДАНИЕ ОТЕЛЯ (Только для роли ADMIN)
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Hotel createHotel(@RequestBody Hotel hotel) {
        return hotelRepository.save(hotel);
    }
}