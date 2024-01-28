package xyz.adolphium.caws.dto.request;

import jakarta.validation.constraints.NotBlank;

public record EmailSendRequest(@NotBlank String email, @NotBlank String subject, @NotBlank String text) {
}
