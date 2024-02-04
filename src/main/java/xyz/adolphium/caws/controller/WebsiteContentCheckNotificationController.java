/**
 * Copyright 2024 Patrick Täsler
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package xyz.adolphium.caws.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import xyz.adolphium.caws.dto.request.ContentNotificationRequest;
import xyz.adolphium.caws.service.WebsiteContentCheckService;

@RestController
@RequiredArgsConstructor
public class WebsiteContentCheckNotificationController {

    private final WebsiteContentCheckService notificationService;

    @PostMapping("website-content-check")
    ResponseEntity<Void> registerWebsiteContentCheck(@NotNull @Valid @RequestBody ContentNotificationRequest request) {
        notificationService.registerWebsiteContentCheck(request);
        return ResponseEntity.accepted()
                .build();
    }
}
