package com.takeaway.gameofthree.api.endpoint;

import com.takeaway.gameofthree.util.Constants;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ServiceAvailabilityEndPoint {

    @GetMapping(value = Constants.GET_SERVICE_AVAILABILITY_API_PATH)
    public boolean getAvailability() {
        return true;
    }
}
