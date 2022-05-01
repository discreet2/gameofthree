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
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(value = WebEndPoint.class)
public class WebEndPointTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GameService gameService;

    @Test
    public void gameStartApiShouldReturnUnProcessableEntityWhenSecondPlayerIsUnavailable() throws Exception {
        String expectedErrorCode = "102";
        Mockito.when(gameService.start(any())).thenThrow(SecondPlayerNotAvailableException.class);
        mockMvc.perform(post(Constants.GAME_START_API_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.reason").value(expectedErrorCode));
    }

    @Test
    public void gameStartApiShouldReturnSuccessWhenGameStarted() throws Exception {
        GameResponse mockGameResponse = GameResponse.builder()
                .message("")
                .gameStatus(GameStatus.RUNNING)
                .build();

        Mockito.when(gameService.start(any())).thenReturn(mockGameResponse);
        mockMvc.perform(post(Constants.GAME_START_API_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isOk());
    }

    @Test
    public void gameStartApiShouldReturnSuccessWithManualUserInput() throws Exception {
        GameResponse mockGameResponse = GameResponse.builder()
                .message("")
                .gameStatus(GameStatus.RUNNING)
                .build();
        String request = "{" +
                "    \"startingNumber\":123" +
                "}";

        Mockito.when(gameService.start(any())).thenReturn(mockGameResponse);
        ResultActions resultActions = mockMvc.perform(post(Constants.GAME_START_API_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(request))
                .andExpect(status().isOk());
    }

}
