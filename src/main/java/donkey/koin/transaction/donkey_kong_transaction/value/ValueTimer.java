package donkey.koin.transaction.donkey_kong_transaction.value;


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
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;


@Component
public class ValueTimer {

    private static final Logger log = LoggerFactory.getLogger(ValueTimer.class);
    private static final String exchangeServiceUrl = "http://localhost:5000/";

    private final ValueRepository repository;

    private final PurchaseTriggerRepository triggerRepository;

    @Autowired
    public ValueTimer(ValueRepository repository, PurchaseTriggerRepository triggerRepository) {
        this.repository = repository;
        this.triggerRepository = triggerRepository;
    }

    @Scheduled(cron = "*/10 * * * * *")
    public void reportCurrentValue() {
        int cents = ThreadLocalRandom.current().nextInt(1000, 2000);
        Instant instant = Instant.now();
//        System.out.println(instant);
        instant = TimeManagement.deleteNano(instant);
        Value value = new Value(instant, cents);
        repository.save(value);
        log.info("Current Cryptocurrency Value: " + value.getCents() + " from time: " + value.getDate());

        checkForOrderedPurchase(value);

    }

    public void checkForOrderedPurchase(Value value) {
        //FOR BUYING
        List<PurchaseTrigger> purchaseTriggers = triggerRepository.findByLimitGreaterThanEqualAndActionEquals(value.getCents(), PurchaseTriggerAction.BUY);
        System.out.println(purchaseTriggers);

        //check for coin amount
        RestTemplate restTemplate = new RestTemplate();
        String purchaseUrl = exchangeServiceUrl + "purchase";
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
//                    .put("lastKoinValue", value.getCents())
//                    .put("transactionTime", value.getDate())
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
