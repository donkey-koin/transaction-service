package donkey.koin.transaction.donkey_kong_transaction.values;


import donkey.koin.transaction.donkey_kong_transaction.values.value.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.concurrent.ThreadLocalRandom;


@Component
public class ValueTimer {

    private static final Logger log = LoggerFactory.getLogger(ValueTimer.class);

    private final ValueRepository repository;

    @Autowired
    public ValueTimer(ValueRepository repository) {
        this.repository = repository;
    }

    @Scheduled(cron = "0 */1 * * * *")
    public void reportCurrentValue() {
        int cents = ThreadLocalRandom.current().nextInt(1000, 2000);
        Value value = new Value(LocalDateTime.now().withNano(0), cents);
        repository.insert(value);
        log.info("Current Cryptocurrency Value: " + value.getCents() + " from time: " + value.getDate());
        log.info("founded");
        for (Value val : repository.findAll()) {
            log.info(val.toString());
        }
    }
}
