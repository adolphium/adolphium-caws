package xyz.adolphium.caws.controller;

import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.net.URL;

@Service
public class WebsiteContentServiceImpl implements WebsiteContentService {

    @Value("${caws.website-parse.download.timeout-ms}")
    private static int WEBSITE_PARSE_DOWNLOAD_TIMEOUT_MS;

    @Override
    public boolean isContentPresent(URL url, String content) {
        try {
            return Jsoup.parse(url, WEBSITE_PARSE_DOWNLOAD_TIMEOUT_MS)
                    .html().contains(content);
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE);
        }
    }
}
