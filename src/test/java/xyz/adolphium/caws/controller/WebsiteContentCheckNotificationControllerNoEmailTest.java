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

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import xyz.adolphium.caws.service.MailService;
import xyz.adolphium.caws.service.WebsiteContentService;

import java.net.URL;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class WebsiteContentCheckNotificationControllerNoEmailTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MailService mailService;

    @MockBean
    private WebsiteContentService websiteContentService;

    @Test
    @SneakyThrows
    void shouldSendEmailWhenContentIsAvailable() {
        var url = "https://adolphium.xyz";
        var checkedContent = "just messing around";
        when(websiteContentService.isContentPresent(new URL(url), checkedContent))
                .thenReturn(false);
        var content = """
                {
                   "contactDataDTO": {
                     "email": "test@adolphium.xyz"
                   },
                   "contentCheckDTO": {
                     "url": "https://adolphium.xyz",
                     "content": "just messing around"
                   }
                 }
                """;
        mockMvc.perform(post("/caws/website-content-check/notification")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                        .contextPath("/caws"))
                .andExpect(status().isAccepted());
        verify(mailService, never()).sendMailTo(anyString(), anyString(), anyString());
    }
}