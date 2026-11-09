package com.example.bookingservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;

@FeignClient(name = "hotel-service")
public interface HotelClient {
    @PostMapping("/api/rooms/{id}/confirm-availability")
    void confirmAvailability(@PathVariable("id") Long id,
                             @RequestParam("start") LocalDate start,
                             @RequestParam("end") LocalDate end,
                             @RequestParam("requestId") String requestId);
}