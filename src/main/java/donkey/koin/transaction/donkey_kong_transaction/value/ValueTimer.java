package donkey.koin.transaction.donkey_kong_transaction.value;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;


@Component
public class ValueTimer {

    private static final Logger log = LoggerFactory.getLogger(ValueTimer.class);

    @Resource
    private Value value;


    @Scheduled(fixedRate = 60000)
    public void reportCurrentValue() {
        int val = ThreadLocalRandom.current().nextInt(1000, 2000);
        value.setCents(val);
        value.setDate(new Date());
        log.info("Current Cryptocurrency Value: " + value.getCents() + " from time: " + value.getDate());
    }
}
