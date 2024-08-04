package xyz.adolphium.caws.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.adolphium.caws.service.OlxPageCheckerService;

import java.net.URL;

@RestController
@RequiredArgsConstructor
public class OlxPageCheckerController {

    private final OlxPageCheckerService olxPageCheckerService;

    @GetMapping("olx/show-prices")
    void getPrices(@Param("url") URL url) {
        olxPageCheckerService.checkMainPageAndSendNotificationIfResultFound(url);
    }
}
