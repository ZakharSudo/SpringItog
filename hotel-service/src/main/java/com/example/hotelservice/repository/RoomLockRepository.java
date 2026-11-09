package com.example.hotelservice.repository;

import com.example.hotelservice.entity.RoomLock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDate;

public interface RoomLockRepository extends JpaRepository<RoomLock, Long> {

    // Проверка: пересекаются ли даты с уже существующими блокировками
    @Query("SELECT COUNT(r) > 0 FROM RoomLock r WHERE r.roomId = :roomId " +
            "AND r.startDate < :endDate AND r.endDate > :startDate")
    boolean existsOverlappingLock(@Param("roomId") Long roomId,
                                  @Param("startDate") LocalDate startDate,
                                  @Param("endDate") LocalDate endDate);

    boolean existsByRequestId(String requestId);

    void deleteByRequestId(String requestId);
}