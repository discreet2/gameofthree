package com.takeaway.gameofthree.application.dao;

import com.takeaway.gameofthree.api.model.MoveRequest;
import com.takeaway.gameofthree.dto.GameResponse;
import com.takeaway.gameofthree.exception.SecondPlayerNotAvailableException;
import com.takeaway.gameofthree.util.Constants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Repository
@RequiredArgsConstructor
public class ExternalApiDaoImpl implements ExternalApiDao {
    private final RestTemplate restTemplate;

    @Override
    public boolean checkAvailabilityOfSecondPlayer(String baseUrl) {
        String url = new StringBuilder()
                .append(baseUrl)
                .append(Constants.URL_SEPARATOR)
                .append(Constants.GET_SERVICE_AVAILABILITY_API_PATH)
                .toString();
        try {
            Boolean availability = restTemplate.getForObject(url, Boolean.class);
            return availability;
        } catch (Exception e) {
            log.warn("Failed to reach other player due to {}", e);
        }
        return false;
    }

    @Override
    public GameResponse currentPlayerMove(String baseUrl, int number) {
        String url = new StringBuilder()
                .append(baseUrl)
                .append(Constants.URL_SEPARATOR)
                .append(Constants.MOVE_REQUEST_API_PATH)
                .toString();
        MoveRequest moveRequest = MoveRequest.builder()
                .number(number)
                .build();
        try {
            GameResponse gameResponse = restTemplate.postForObject(url, moveRequest, GameResponse.class);
            return gameResponse;
        } catch (Exception e) {
            log.error("Move request failed due to ", e);
            throw new SecondPlayerNotAvailableException(e.getMessage());
        }
    }
}
