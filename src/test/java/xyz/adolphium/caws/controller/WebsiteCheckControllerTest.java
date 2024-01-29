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
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import xyz.adolphium.caws.service.WebsiteContentService;

import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.stream.Stream;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class WebsiteCheckControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WebsiteContentService websiteContentService;

    public static Stream<Arguments> contentSearch() {
        return Stream.of(Arguments.of(true, """
                        {"isContentAvailable":true}
                        """),
                Arguments.of(true, """
                        {"isContentAvailable":true}
                        """));
    }

    @ParameterizedTest
    @MethodSource("contentSearch")
    @SneakyThrows
    void contentAvailable(boolean isContentPresent, String expectedResponse) {
        var url = "https://adolphium.xyz";
        var content = "just messing around";
        when(websiteContentService.isContentPresent(new URL(url), content))
                .thenReturn(isContentPresent);
        var result = mockMvc.perform(get("/caws/website-content-check")
                        .contextPath("/caws")
                        .param("url", url)
                        .param("content", content))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);
        JSONAssert.assertEquals(expectedResponse, result, true);
    }
}