package com.supportdesk.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ErrorResponseDTO {

    private int status;
    private String message;
    private LocalDateTime timestamp;

}