package com.example.hotelservice.controller;

import com.example.hotelservice.entity.Room;
import com.example.hotelservice.entity.RoomLock;
import com.example.hotelservice.repository.RoomLockRepository;
import com.example.hotelservice.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/rooms")
@RequiredArgsConstructor
public class RoomController {

    private final RoomRepository roomRepository;
    private final RoomLockRepository roomLockRepository;

    // Публичный просмотр номеров
    @GetMapping
    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }

    // Публичный просмотр рекомендованных номеров
    @GetMapping("/recommend")
    public List<Room> getRecommendedRooms() {
        return roomRepository.findAllByOrderByTimesBookedAscIdAsc();
    }

    // СОЗДАНИЕ НОМЕРА (Только для роли ADMIN)
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Room createRoom(@RequestBody Room room) {
        return roomRepository.save(room);
    }

    // --- САГА: ПОДТВЕРЖДЕНИЕ (Внутренний вызов) ---
    @PostMapping("/{id}/confirm-availability")
    @Transactional
    public void confirmAvailability(
            @PathVariable Long id,
            @RequestParam("start") LocalDate start,
            @RequestParam("end") LocalDate end,
            @RequestParam("requestId") String requestId) {

        if (roomLockRepository.existsByRequestId(requestId)) {
            return;
        }

        if (roomLockRepository.existsOverlappingLock(id, start, end)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Room is already booked");
        }

        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Room not found"));

        room.setTimesBooked(room.getTimesBooked() + 1);
        roomRepository.save(room);

        RoomLock lock = new RoomLock();
        lock.setRoomId(id);
        lock.setStartDate(start);
        lock.setEndDate(end);
        lock.setRequestId(requestId);
        roomLockRepository.save(lock);
    }

    // --- САГА: КОМПЕНСАЦИЯ (Внутренний вызов) ---
    @PostMapping("/{id}/release")
    @Transactional
    public void release(@PathVariable Long id, @RequestParam("requestId") String requestId) {
        roomLockRepository.deleteByRequestId(requestId);
    }
}