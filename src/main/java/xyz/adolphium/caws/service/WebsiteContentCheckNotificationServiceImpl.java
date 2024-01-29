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

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import xyz.adolphium.caws.dto.request.ContentCheckDTO;
import xyz.adolphium.caws.dto.request.ContentNotificationContactDataDTO;

@Service
@RequiredArgsConstructor
public class WebsiteContentCheckNotificationServiceImpl implements WebsiteContentCheckNotificationService {

    private final MailService mailService;
    private final WebsiteContentService websiteContentService;

    @Value("${caws.mail.content-check-notification.subject.pre}")
    private String EMAIL_CONTENT_CHECK_NOTIFICATION_SUBJECT_PRE;
    @Value("${caws.mail.content-check-notification.text.pre}")
    private String EMAIL_CONTENT_CHECK_NOTIFICATION_TEXT_PRE;

    @Override
    public void notifyIfContentIsPresent(ContentNotificationContactDataDTO contactDataDTO, ContentCheckDTO contentCheckDTO) {
        if (!websiteContentService.isContentPresent(contentCheckDTO.url(), contentCheckDTO.content())) {
           return;
        }
        var subject = getContentFoundSubject(contentCheckDTO);
        var text = getContentFoundText(contentCheckDTO);
        mailService.sendMailTo(contactDataDTO.email(),
                subject,
                text);
    }

    private String getContentFoundText(ContentCheckDTO contentCheckDTO) {
        return EMAIL_CONTENT_CHECK_NOTIFICATION_TEXT_PRE + "\n\n" + contentCheckDTO.content();
    }

    private String getContentFoundSubject(ContentCheckDTO contentCheckDTO) {
        return EMAIL_CONTENT_CHECK_NOTIFICATION_SUBJECT_PRE + " " + contentCheckDTO.url();
    }
}
