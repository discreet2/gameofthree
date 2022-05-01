package com.takeaway.gameofthree.api.endpoint;

import com.takeaway.gameofthree.api.model.BeginRequest;
import com.takeaway.gameofthree.application.service.GameService;
import com.takeaway.gameofthree.dto.GameResponse;
import com.takeaway.gameofthree.util.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequiredArgsConstructor
public class WebEndPoint {
    private final GameService gameService;

    @Value("${spring.application.name}")
    private String appName;


    @GetMapping("/index")
    public String showGameStartPage(Model model) {
        BeginRequest beginRequest = BeginRequest.builder()
                .playerName(appName)
                .actionUrl(Constants.GAME_START_API_PATH)
                .startingNumber(null)
                .build();
        model.addAttribute("beginRequest", beginRequest);
        return "index";
    }


    @PostMapping(value = Constants.GAME_START_API_PATH)
    public String start(@ModelAttribute("beginRequest") BeginRequest beginRequest, HttpServletRequest request) {
        gameService.start(beginRequest);
        return "success";
    }
}
