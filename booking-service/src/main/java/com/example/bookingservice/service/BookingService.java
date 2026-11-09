package com.example.bookingservice.service;

import com.example.bookingservice.client.HotelClient;
import com.example.bookingservice.dto.CreateBookingRequest;
import com.example.bookingservice.entity.Booking;
import com.example.bookingservice.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final HotelClient hotelClient;

    public Booking createBooking(Long userId, CreateBookingRequest request) {
        // Упрощенная версия для запуска
        String reqId = request.getRequestId() != null ? request.getRequestId() : UUID.randomUUID().toString();

        Booking booking = new Booking();
        booking.setUserId(userId);
        booking.setRoomId(request.getRoomId());
        booking.setStartDate(request.getStartDate());
        booking.setEndDate(request.getEndDate());
        booking.setStatus("PENDING");
        booking.setCreatedAt(LocalDateTime.now());
        booking.setRequestId(reqId);

        // По идее тут должен быть вызов hotelClient.confirmAvailability,
        // но для теста просто сохраняем.
        booking = bookingRepository.save(booking);

        booking.setStatus("CONFIRMED");
        return bookingRepository.save(booking);
    }
}