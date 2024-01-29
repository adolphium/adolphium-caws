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

import com.icegreen.greenmail.configuration.GreenMailConfiguration;
import com.icegreen.greenmail.junit5.GreenMailExtension;
import com.icegreen.greenmail.util.ServerSetupTest;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import xyz.adolphium.caws.service.WebsiteContentService;

import java.net.URL;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class WebsiteContentCheckNotificationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @RegisterExtension
    static GreenMailExtension greenMail = new GreenMailExtension(ServerSetupTest.SMTP)
            .withConfiguration(GreenMailConfiguration.aConfig().withUser("test@adolphium.xyz", "super_secret_test_password"))
            .withPerMethodLifecycle(false);

    @MockBean
    private WebsiteContentService websiteContentService;

    @Test
    @SneakyThrows
    void shouldSendEmailWhenContentIsAvailable() {
        var url = "https://adolphium.xyz";
        var checkedContent = "just messing around";
        when(websiteContentService.isContentPresent(new URL(url), checkedContent))
                .thenReturn(true);
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
        var receivedMessages = greenMail.getReceivedMessages();
        assertThat(receivedMessages)
                .hasSize(1);
        var receivedMessage = receivedMessages[0];
        assertAll(() -> assertThat(receivedMessage.getSubject()).isEqualTo("Found searched content for url: https://adolphium.xyz"),
                () -> assertThat(receivedMessage.getContent()).asString().contains("Searched content:"),
                () -> assertThat(receivedMessage.getContent()).asString().contains("just messing around"),
                () -> assertThat(receivedMessage.getAllRecipients()).hasSize(1));
        assertThat(receivedMessage.getAllRecipients()[0].toString()).isEqualTo("test@adolphium.xyz");
    }
}