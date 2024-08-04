package xyz.adolphium.caws.service;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class OlxPageCheckerServiceImpl implements OlxPageCheckerService {
    private final int WEBSITE_PARSE_DOWNLOAD_TIMEOUT_MS = 100000;
    private final Logger logger = Logger.getLogger(OlxPageCheckerServiceImpl.class.getName());

    private final MailService mailService;

    @Value("${olx.notification.recipient_mail}")
    private String notificationRecipientEmail;
    @Value("${olx.desired_location}")
    private final String DESIRED_LOCATION = "Bielsko-Biała";
    @Value("${olx.max_price_pln}")
    private BigDecimal MAX_PRICE_PLN;

    @Override
    public void checkMainPageAndSendNotificationIfResultFound(URL url) {
        var offersBelowPriceTarget = findOffersBelowPriceTarget(url);
        sendNotificationEmails(offersBelowPriceTarget);
    }

    private void sendNotificationEmails(Collection<OlxResultDto> filteredOffers) {
        var offersBelowPriceTarget = filterIfAlreadyNotifiedAndPriceDidNotChange(filteredOffers);
        mailService.sendMailTo(notificationRecipientEmail, "new offers", offersBelowPriceTarget.toString());
    }

    private void logWebsiteParseException(IOException e) {
        logger.severe("Exception while website parsing\nMessage \n" + e.getMessage());
    }


    private Collection<OlxResultDto> filterIfAlreadyNotifiedAndPriceDidNotChange(Collection<OlxResultDto> offersBelowPriceTarget) {
        return offersBelowPriceTarget;
    }

    private Collection<OlxResultDto> findOffersBelowPriceTarget(URL url) {
        try {
            Document doc = Jsoup.parse(url, WEBSITE_PARSE_DOWNLOAD_TIMEOUT_MS);
            return findMatchingOffers(doc);
        } catch (IOException e) {
            logWebsiteParseException(e);
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE);
        }
    }

    private Collection<OlxResultDto> findMatchingOffers(Document doc) {
        var resultDtos = doc.select("p[data-testid=ad-price]")
                .stream()
                .map(el -> OlxResultDto.builder()
                        .helperElement(el)
                        .build()).toList();
        for (OlxResultDto olxResultDto : resultDtos) {
            var link = olxResultDto.helperElement.parent()
                    .selectFirst("a")
                    .attr("href")
                    .replace("/d/oferta/", "https://olx.pl/d/oferta/");
            olxResultDto.setLink(link);
            var price = olxResultDto.helperElement.text()
                    .split(" zł")[0]
                    .replace(" ", "");
            try {
                olxResultDto.setPrice(new BigDecimal(price));
            } catch (Exception e) {
                logger.severe("error writing price");
            }
        }
        var filteredByLocation = filterByLocation(resultDtos);
        return filterByPrice(filteredByLocation);
    }

    private Collection<OlxResultDto> filterByLocation(List<OlxResultDto> olxResultDtos) {
        return olxResultDtos.stream()
                .filter(x -> x.helperElement.parent().parent().select("p[data-testid='location-date']").toString().contains(DESIRED_LOCATION)).toList();
    }

    private Collection<OlxResultDto> filterByPrice(Collection<OlxResultDto> olxResultDtos) {
        return olxResultDtos.stream()
                .filter(x -> Objects.nonNull(x.getPrice()))
                .filter(this::smallerThanMaxPrice)
                .sorted(Comparator.comparing(OlxResultDto::getPrice))
                .toList();
    }

    private boolean smallerThanMaxPrice(OlxResultDto x) {
        return x.getPrice().compareTo(MAX_PRICE_PLN) < 0;
    }

    @Builder
    static
    class OlxResultDto {
        private BigDecimal price;
        private String link;
        private Element helperElement;

        void setPrice(BigDecimal price) {
            this.price = price;
        }

        void setLink(String link) {
            this.link = link;
        }


        BigDecimal getPrice() {
            return price;
        }

        @Override
        public String toString() {
            return """
                    { "link":""" + link + """
                    , "price":""" + price + """
                    }
                    """;
        }
    }
}
