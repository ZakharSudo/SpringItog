package com.example.hotelservice.service;

import com.example.hotelservice.entity.Room;
import com.example.hotelservice.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;

    // Все номера (без сортировки)
    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }

    // Рекомендованные номера (отсортированы по загруженности)
    public List<Room> getRecommendedRooms() {
        return roomRepository.findAllByOrderByTimesBookedAscIdAsc();
    }
}