package xyz.adolphium.caws.service;

import java.net.URL;

public interface OlxPageCheckerService {


    void checkMainPageAndSendNotificationIfResultFound(URL url);
}
