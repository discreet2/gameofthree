package com.takeaway.gameofthree.application.dao;

import com.takeaway.gameofthree.dto.GameResponse;

public interface ExternalApiDao {
    boolean checkAvailabilityOfSecondPlayer(String baseUrl);

    GameResponse currentPlayerMove(String baseUrl, int number);
}
