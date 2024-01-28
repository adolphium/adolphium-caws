package xyz.adolphium.caws.controller;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URL;

@RestController
@RequiredArgsConstructor
public class WebsiteCheckController {

    private final WebsiteContentService websiteContentService;

    @GetMapping("website-content-check")
    ResponseEntity<Boolean> contentAvailable(@RequestParam("url") @NotNull URL url,
                                             @RequestParam("content") @NotBlank String content) {
        return ResponseEntity.ok(websiteContentService.isContentPresent(url, content));
    }
}
