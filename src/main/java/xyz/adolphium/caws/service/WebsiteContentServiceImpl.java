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

package xyz.adolphium.caws.service;

import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.net.URL;
import java.util.logging.Logger;

@Service
public class WebsiteContentServiceImpl implements WebsiteContentService {

    private Logger logger = Logger.getLogger(WebsiteContentServiceImpl.class.getName());
    @Value("${caws.website-parse.download.timeout-ms}")
    private static int WEBSITE_PARSE_DOWNLOAD_TIMEOUT_MS;

    @Override
    public boolean isContentPresent(URL url, String content) {
        try {
            return Jsoup.parse(url, WEBSITE_PARSE_DOWNLOAD_TIMEOUT_MS)
                    .html()
                    .contains(content);
        } catch (IOException e) {
            logWebsiteParseException(e);
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE);
        }
    }

    private void logWebsiteParseException(IOException e) {
        logger.severe("Exception while website parsing\nMessage \n"+ e.getMessage());
    }
}
