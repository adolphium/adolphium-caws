package xyz.adolphium.paws.controller;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import xyz.adolphium.paws.dto.request.EmailSendRequest;
import xyz.adolphium.paws.service.MailService;

@RequiredArgsConstructor
@RestController
public class MailController {


    private final MailService mailService;

    @PostMapping("/notification")
    ResponseEntity<Void> sendEmail(@RequestBody @NotNull EmailSendRequest request) {
        mailService.sendMailTo(request.email(), request.subject(), request.text());
        return ResponseEntity.accepted()
                .build();
    }
}
