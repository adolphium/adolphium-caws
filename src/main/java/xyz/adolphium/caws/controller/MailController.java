package xyz.adolphium.caws.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import xyz.adolphium.caws.dto.request.EmailSendRequest;
import xyz.adolphium.caws.service.MailService;

@RequiredArgsConstructor
@RestController
@Validated
public class MailController {


    private final MailService mailService;

    @PostMapping("/notification")
    ResponseEntity<Void> sendEmail(@RequestBody @NotNull @Valid  EmailSendRequest request) {
        mailService.sendMailTo(request.email(), request.subject(), request.text());
        return ResponseEntity.accepted()
                .build();
    }
}
