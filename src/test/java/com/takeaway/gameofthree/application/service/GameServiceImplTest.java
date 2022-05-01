package com.takeaway.gameofthree.application.service;

import com.takeaway.gameofthree.api.model.BeginRequest;
import com.takeaway.gameofthree.api.model.MoveRequest;
import com.takeaway.gameofthree.application.dao.ExternalApiDao;
import com.takeaway.gameofthree.application.dao.ExternalApiDaoImpl;
import com.takeaway.gameofthree.dto.GameResponse;
import com.takeaway.gameofthree.dto.GameStatus;
import com.takeaway.gameofthree.exception.SecondPlayerNotAvailableException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
public class GameServiceImplTest {
    @Value("${spring.application.name}")
    private String appName;
    @Value("${second.player.base.url}")
    private String secondPlayerBaseUrl;

    @Value("${beginning.random.number.upper.limit}")
    public int beginningRandomNumberUpperLimit;

    private GameServiceImpl gameService;

    @MockBean
    private ExternalApiDao externalApiDao;

    @BeforeEach
    void init() {
        externalApiDao = Mockito.mock(ExternalApiDaoImpl.class);
        gameService = new GameServiceImpl(externalApiDao, appName, secondPlayerBaseUrl, beginningRandomNumberUpperLimit);
    }

    @Test
    void shouldThrowErrorWhenSecondPlayerIsUnavailable() {
        BeginRequest beginRequest = BeginRequest.builder().build();
        Mockito.when(externalApiDao.checkAvailabilityOfSecondPlayer(secondPlayerBaseUrl)).thenReturn(false);
        Assertions.assertThrows(SecondPlayerNotAvailableException.class, () -> gameService.start(beginRequest));
    }

    @Test
    void shouldReturnGameStatusRunningForGameStart() {
        BeginRequest beginRequest = BeginRequest.builder().build();
        Mockito.when(externalApiDao.checkAvailabilityOfSecondPlayer(secondPlayerBaseUrl)).thenReturn(true);
        Assertions.assertEquals(GameStatus.RUNNING, gameService.start(beginRequest).getGameStatus());
    }

    @Test
    void testGameMoveWhenSecondPlayerIsUnAvailable() {
        MoveRequest moveRequest = MoveRequest.builder()
                .number(123)
                .build();
        Mockito.when(externalApiDao.checkAvailabilityOfSecondPlayer(secondPlayerBaseUrl)).thenReturn(false);
        Assertions.assertEquals(GameStatus.ENDED, gameService.move(moveRequest).getGameStatus());
    }

    @Test
    void testGameMoveWhenSecondPlayerDiscoveredButMoveRequestFailed() {
        MoveRequest moveRequest = MoveRequest.builder()
                .number(123)
                .build();
        int currentNumber = moveRequest.getNumber();
        int nextNumber;
        if (currentNumber % 3 == 0) {
            nextNumber = currentNumber / 3;
        } else if ((currentNumber + 1) % 3 == 0) {
            nextNumber = (currentNumber + 1) / 3;
        } else {
            nextNumber = (currentNumber - 1) / 3;
        }
        Mockito.when(externalApiDao.checkAvailabilityOfSecondPlayer(secondPlayerBaseUrl)).thenReturn(true);
        Mockito.when(externalApiDao.currentPlayerMove(secondPlayerBaseUrl, nextNumber)).thenThrow(SecondPlayerNotAvailableException.class);
        Assertions.assertThrows(SecondPlayerNotAvailableException.class, () -> gameService.move(moveRequest));
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -2, -10, -100})
    void testGameMoveWhenMoveNumberIsZeroOrNegative(int value) {
        MoveRequest moveRequest = MoveRequest.builder()
                .number(value)
                .build();
        Assertions.assertEquals(GameStatus.ENDED, gameService.move(moveRequest).getGameStatus());
    }


    @ParameterizedTest
    @ValueSource(ints = {124, 4736, 93837, 2736})
    void testGameMoveRunningForValidMoveRequest(int value) {
        MoveRequest moveRequest = MoveRequest.builder()
                .number(value)
                .build();
        GameResponse mockGameResponse = GameResponse.builder()
                .message("")
                .gameStatus(GameStatus.RUNNING)
                .build();
        int currentNumber = moveRequest.getNumber();
        int nextNumber;
        if (currentNumber % 3 == 0) {
            nextNumber = currentNumber / 3;
        } else if ((currentNumber + 1) % 3 == 0) {
            nextNumber = (currentNumber + 1) / 3;
        } else {
            nextNumber = (currentNumber - 1) / 3;
        }
        Mockito.when(externalApiDao.checkAvailabilityOfSecondPlayer(secondPlayerBaseUrl)).thenReturn(true);
        Mockito.when(externalApiDao.currentPlayerMove(secondPlayerBaseUrl, nextNumber)).thenReturn(mockGameResponse);
        Assertions.assertEquals(GameStatus.RUNNING, gameService.move(moveRequest).getGameStatus());
    }

    @ParameterizedTest
    @ValueSource(ints = {3, 4, 2})
    void testWinningNumbers(int value) {
        MoveRequest moveRequest = MoveRequest.builder()
                .number(value)
                .build();
        Mockito.when(externalApiDao.checkAvailabilityOfSecondPlayer(secondPlayerBaseUrl)).thenReturn(true);
        Assertions.assertEquals(GameStatus.COMPLETED, gameService.move(moveRequest).getGameStatus());
    }

}
