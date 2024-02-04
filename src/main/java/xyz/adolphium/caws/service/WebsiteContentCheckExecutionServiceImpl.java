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
import org.springframework.stereotype.Service;
import xyz.adolphium.caws.entity.WebsiteCheck;

@Service
@RequiredArgsConstructor
public class WebsiteContentCheckExecutionServiceImpl implements WebsiteContentCheckExecutionService {


    private final WebsiteContentService websiteContentService;
    private final WebsiteCheckSaveService websiteCheckSaveService;
    private final WebsiteContentCheckNotificationService websiteContentCheckNotificationService;

    @Override
    public void performCheck(WebsiteCheck websiteCheck) {
        var contentFound = websiteContentService.isContentPresent(websiteCheck.getUrl(), websiteCheck.getContent());
        websiteCheckSaveService.update(websiteCheck, contentFound);
        if (contentFound) {
            websiteContentCheckNotificationService.notifyContentIsPresent(websiteCheck);
        }
    }
}
