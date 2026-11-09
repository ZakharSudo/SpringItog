package com.example.hotelservice.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "room_locks")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class RoomLock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long roomId;
    private LocalDate startDate;
    private LocalDate endDate;
    private String requestId; // Для идемпотентности
}