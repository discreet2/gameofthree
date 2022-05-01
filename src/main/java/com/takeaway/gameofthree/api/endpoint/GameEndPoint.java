package com.takeaway.gameofthree.api.endpoint;

import com.takeaway.gameofthree.api.model.MoveRequest;
import com.takeaway.gameofthree.application.service.GameService;
import com.takeaway.gameofthree.dto.GameResponse;
import com.takeaway.gameofthree.util.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class GameEndPoint {
    private final GameService gameService;

    @PostMapping(value = Constants.MOVE_REQUEST_API_PATH)
    public GameResponse move(@RequestBody @Valid MoveRequest moveRequest) {
        return gameService.move(moveRequest);
    }
}
