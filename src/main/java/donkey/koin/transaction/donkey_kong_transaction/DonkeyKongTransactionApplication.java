package donkey.koin.transaction.donkey_kong_transaction;

import donkey.koin.transaction.donkey_kong_transaction.values.ValueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@ComponentScan({"donkey.koin.transaction"})
@SpringBootApplication//(exclude = {MongoAutoConfiguration.class, MongoDataAutoConfiguration.class})
public class DonkeyKongTransactionApplication implements CommandLineRunner {


    private final ValueRepository valueRepository;

    @Autowired
    public DonkeyKongTransactionApplication(ValueRepository valueRepository) {
        this.valueRepository = valueRepository;
    }

    public static void main(String[] args) {

        SpringApplication.run(DonkeyKongTransactionApplication.class, args);
    }


    @Override
    public void run(String... args) throws Exception {
        valueRepository.deleteAll();
    }
}
