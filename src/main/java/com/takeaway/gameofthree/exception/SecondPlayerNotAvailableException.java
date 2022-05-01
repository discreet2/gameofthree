package com.takeaway.gameofthree.exception;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SecondPlayerNotAvailableException extends RuntimeException {
    private String message;
}