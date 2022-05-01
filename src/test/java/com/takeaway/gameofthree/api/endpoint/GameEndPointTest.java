package com.takeaway.gameofthree.api.endpoint;

import com.takeaway.gameofthree.application.service.GameService;
import com.takeaway.gameofthree.dto.GameResponse;
import com.takeaway.gameofthree.dto.GameStatus;
import com.takeaway.gameofthree.exception.SecondPlayerNotAvailableException;
import com.takeaway.gameofthree.util.Constants;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(value = GameEndPoint.class)
public class GameEndPointTest {
    public static final String INVALID_API_URL = "/api/worng-url";
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GameService gameService;

    @Test
    public void invalidApiShouldReturnNotFound() throws Exception {
        mockMvc.perform(get(INVALID_API_URL))
                .andExpect(status().isNotFound());
    }

    @Test
    public void gameMoveApiShouldReturnBadRequestForInvalidRequest() throws Exception {
        String expectedErrorCode = "101";
        mockMvc.perform(post(Constants.MOVE_REQUEST_API_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.reason").value(expectedErrorCode));
    }

    @Test
    public void gameMoveApiShouldReturnUnProcessableEntityWhenSecondPlayerIsUnavailable() throws Exception {
        String expectedErrorCode = "102";
        String request = "{" +
                "    \"number\":123" +
                "}";
        Mockito.when(gameService.move(any())).thenThrow(SecondPlayerNotAvailableException.class);
        mockMvc.perform(post(Constants.MOVE_REQUEST_API_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(request))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.reason").value(expectedErrorCode));
    }

    @Test
    public void gameMoveApiShouldReturnSuccess() throws Exception {
        GameResponse mockGameResponse = GameResponse.builder()
                .message("")
                .gameStatus(GameStatus.RUNNING)
                .build();
        String request = "{" +
                "    \"number\":123" +
                "}";

        Mockito.when(gameService.move(any())).thenReturn(mockGameResponse);
        mockMvc.perform(post(Constants.MOVE_REQUEST_API_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(request))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.gameStatus").value(GameStatus.RUNNING.name()));
    }
}
