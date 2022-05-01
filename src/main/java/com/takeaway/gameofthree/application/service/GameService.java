package com.takeaway.gameofthree.application.service;

import com.takeaway.gameofthree.api.model.BeginRequest;
import com.takeaway.gameofthree.api.model.MoveRequest;
import com.takeaway.gameofthree.dto.GameResponse;

public interface GameService {
    GameResponse start(BeginRequest beginRequest);

    GameResponse move(MoveRequest moveRequest);
}
