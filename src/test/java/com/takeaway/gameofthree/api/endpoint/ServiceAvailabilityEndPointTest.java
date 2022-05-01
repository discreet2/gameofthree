package com.takeaway.gameofthree.api.endpoint;

import com.takeaway.gameofthree.dto.GameResponse;
import com.takeaway.gameofthree.dto.GameStatus;
import com.takeaway.gameofthree.util.Constants;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(value = ServiceAvailabilityEndPoint.class)
public class ServiceAvailabilityEndPointTest {
    public static final String INVALID_API_URL = "/api/worng-url";
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void shouldReturnNotFound() throws Exception {
        mockMvc.perform(get(INVALID_API_URL))
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldReturnSuccess() throws Exception {
        mockMvc.perform(get(Constants.GET_SERVICE_AVAILABILITY_API_PATH))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }
}
