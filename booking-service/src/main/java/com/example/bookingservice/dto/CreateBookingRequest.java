package com.example.bookingservice.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDate;

@Data
public class CreateBookingRequest {
    private Long roomId;

    @NotNull(message = "Дата начала обязательна")
    @FutureOrPresent(message = "Дата начала не может быть в прошлом")
    private LocalDate startDate;

    @NotNull(message = "Дата окончания обязательна")
    @FutureOrPresent(message = "Дата окончания не может быть в прошлом")
    private LocalDate endDate;

    private boolean autoSelect;
    private String requestId;
}