package com.takeaway.gameofthree.application.service;

import com.takeaway.gameofthree.api.model.BeginRequest;
import com.takeaway.gameofthree.api.model.MoveRequest;
import com.takeaway.gameofthree.application.dao.ExternalApiDao;
import com.takeaway.gameofthree.dto.GameResponse;
import com.takeaway.gameofthree.dto.GameStatus;
import com.takeaway.gameofthree.exception.SecondPlayerNotAvailableException;
import com.takeaway.gameofthree.util.Constants;
import com.takeaway.gameofthree.util.Utility;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class GameServiceImpl implements GameService {
    private ExternalApiDao externalApiDao;
    private String appName;
    private String secondPlayerBaseUrl;
    public int beginningRandomNumberUpperLimit;

    public GameServiceImpl(ExternalApiDao externalApiDao,
                           @Value("${spring.application.name}") String appName,
                           @Value("${second.player.base.url}") String secondPlayerBaseUrl,
                           @Value("${beginning.random.number.upper.limit}") int beginningRandomNumberUpperLimit) {
        this.externalApiDao = externalApiDao;
        this.appName = appName;
        this.secondPlayerBaseUrl = secondPlayerBaseUrl;
        this.beginningRandomNumberUpperLimit = beginningRandomNumberUpperLimit;
    }

    @Override
    public GameResponse start(BeginRequest beginRequest) {
        int nextNumber;
        if (!externalApiDao.checkAvailabilityOfSecondPlayer(secondPlayerBaseUrl)) {
            throw new SecondPlayerNotAvailableException("Second player is not available yet. Game ended.");
        }
        if (beginRequest.getStartingNumber() != null) {
            nextNumber = beginRequest.getStartingNumber();
            log.info("Second player found! Game started with manual input number={}", nextNumber);
        } else {
            nextNumber = Utility.getRandomNumber(beginningRandomNumberUpperLimit);
            log.info("Second player found! Game started automatically with randomly generated number={}", nextNumber);
        }
        startTheGame(nextNumber);
        return GameResponse.builder()
                .gameStatus(GameStatus.RUNNING)
                .message("Game started")
                .build();
    }

    @Async("threadPoolTaskExecutor")
    public void startTheGame(int nextNumber) {
        externalApiDao.currentPlayerMove(secondPlayerBaseUrl, nextNumber);
    }

    @Override
    public GameResponse move(MoveRequest moveRequest) {
        GameResponse gameResponse;
        int nextNumber;
        int currentNumber = moveRequest.getNumber();
        if (currentNumber <= 0) {
            gameResponse = GameResponse.builder()
                    .gameStatus(GameStatus.ENDED)
                    .message("Game ended due to current move number is less than or equal to 0")
                    .build();
            log.info("Game ended due to current move number={} is less than or equal to 0", currentNumber);
            return gameResponse;
        } else if (currentNumber % 3 == 0) {
            nextNumber = currentNumber / 3;
            log.info("number received={}, value added={}, next number={}", currentNumber, 0, nextNumber);
        } else if ((currentNumber + 1) % 3 == 0) {
            nextNumber = (currentNumber + 1) / 3;
            log.info("number received={}, value added={}, next number={}", currentNumber, 1, nextNumber);
        } else {
            nextNumber = (currentNumber - 1) / 3;
            log.info("number received={}, value added={}, next number={}", currentNumber, -1, nextNumber);
        }

        if (nextNumber == Constants.WINNING_NUMBER) {
            gameResponse = GameResponse.builder()
                    .gameStatus(GameStatus.COMPLETED)
                    .message(appName + " is the winner")
                    .build();
            log.info("Congratulations! you({}) are the winner.", appName);
        } else if (externalApiDao.checkAvailabilityOfSecondPlayer(secondPlayerBaseUrl)) {
            gameResponse = GameResponse.builder()
                    .gameStatus(GameStatus.RUNNING)
                    .message("Game is running")
                    .build();
            externalApiDao.currentPlayerMove(secondPlayerBaseUrl, nextNumber);
        } else {
            String message = "Ops! Second player us unreachable. Game Ended!!!";
            gameResponse = GameResponse.builder()
                    .gameStatus(GameStatus.ENDED)
                    .message(message)
                    .build();
            log.info(message);
        }

        return gameResponse;
    }
}
