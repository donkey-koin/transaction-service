package donkey.koin.transaction.donkey_kong_transaction.value;


import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import donkey.koin.transaction.donkey_kong_transaction.entities.PurchaseTrigger;
import donkey.koin.transaction.donkey_kong_transaction.entities.Value;
import donkey.koin.transaction.donkey_kong_transaction.purchase.trigger.PurchaseTriggerAction;
import donkey.koin.transaction.donkey_kong_transaction.repo.PurchaseTriggerRepository;
import donkey.koin.transaction.donkey_kong_transaction.repo.ValueRepository;
import donkey.koin.transaction.donkey_kong_transaction.utils.TimeManagement;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;


@Component
public class ValueTimer {

    private static final Logger log = LoggerFactory.getLogger(ValueTimer.class);
    private static final String exchangeServiceUrl = "http://localhost:5000/";

    private final ValueRepository repository;

    private final PurchaseTriggerRepository triggerRepository;

    //    private volatile int fuelPrice = 50000;
    private int bitcoinPrice = 500000;


    @Autowired
    public ValueTimer(ValueRepository repository, PurchaseTriggerRepository triggerRepository) {
        this.repository = repository;
        this.triggerRepository = triggerRepository;
    }

    @Scheduled(cron = "*/10 * * * * *")
    public void reportCurrentValue() {
        int randomCents = ThreadLocalRandom.current().nextInt(100, 200);
        int donkeyKoinCalculatedPrice = bitcoinPrice + randomCents;

        Instant instant = Instant.now();
        instant = TimeManagement.deleteNano(instant);
        Value value = new Value(instant, donkeyKoinCalculatedPrice);
        repository.save(value);
        log.info("Current Cryptocurrency Value: " + value.getCents() + " from time: " + value.getDate());
        checkForOrderedPurchase(value);
    }

    private void checkForOrderedPurchase(Value value) {
        checkBuyTriggers(value);
        checkSellTriggers(value);
    }

    @Scheduled(cron = "*/60 * * * * *")
    public void countDonkeyKoinValue() {
        RestTemplate restTemplate = new RestTemplate();
        String bitcoinUrl = "https://api.coindesk.com/v1/bpi/currentprice.json";
        HttpEntity<String> request;
        HttpHeaders headers;
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64)");
        request = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                bitcoinUrl, HttpMethod.GET, request, String.class);
        JsonObject o = new JsonParser().parse(Objects.requireNonNull(response.getBody())).getAsJsonObject();
        try {
            this.bitcoinPrice = (int) (o.get("bpi").getAsJsonObject().get("EUR").getAsJsonObject().get("rate_float").getAsDouble() * 100);
        } catch (Exception e) {
            System.out.println("parsing bitcoin reponse error");
        }
    }


    private void checkBuyTriggers(Value value) {
        List<PurchaseTrigger> purchaseTriggers = triggerRepository.findByLimitGreaterThanEqualAndActionEquals(value.getCents() / 100, PurchaseTriggerAction.BUY);
        postToOrchestration(purchaseTriggers, "purchase");
    }

    private void checkSellTriggers(Value value) {
        List<PurchaseTrigger> purchaseTriggers = triggerRepository.findByLimitLessThanEqualAndActionEquals(value.getCents() / 100, PurchaseTriggerAction.SELL);
        postToOrchestration(purchaseTriggers, "sell");
    }

    private void postToOrchestration(List<PurchaseTrigger> purchaseTriggers, String endpoint) {
        RestTemplate restTemplate = new RestTemplate();
        String purchaseUrl = exchangeServiceUrl + endpoint;
        HttpEntity<String> request;
        String jsonString;
        String response;
        HttpHeaders headers;
        for (PurchaseTrigger trigger : purchaseTriggers) {
            headers = new HttpHeaders();
            headers.add("Authorization", trigger.getToken());
            headers.setContentType(MediaType.APPLICATION_JSON);

            jsonString = new JSONObject()
                    .put("username", trigger.getUsername())
                    .put("moneyAmount", trigger.getCoinAmount())
                    .toString();

            request = new HttpEntity<>(jsonString, headers);
            System.out.println(request);
            try {
                response = restTemplate.postForObject(purchaseUrl, request, String.class);
                System.out.println(response);

                triggerRepository.delete(trigger);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
