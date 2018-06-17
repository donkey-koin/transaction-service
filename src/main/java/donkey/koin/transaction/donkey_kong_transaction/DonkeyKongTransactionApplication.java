package donkey.koin.transaction.donkey_kong_transaction;

import donkey.koin.transaction.donkey_kong_transaction.koin.KoinManager;
import donkey.koin.transaction.donkey_kong_transaction.repo.PurchaseTriggerRepository;
import donkey.koin.transaction.donkey_kong_transaction.repo.TransactionRepository;
import donkey.koin.transaction.donkey_kong_transaction.repo.UTXORepository;
import donkey.koin.transaction.donkey_kong_transaction.repo.ValueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@ComponentScan({"donkey.koin.transaction"})
@SpringBootApplication
public class DonkeyKongTransactionApplication implements CommandLineRunner {

    @Autowired
    KoinManager koinManager;

    private final ValueRepository valueRepository;
    private final PurchaseTriggerRepository purchaseTriggerRepository;


    public DonkeyKongTransactionApplication(ValueRepository valueRepository,
                                            PurchaseTriggerRepository purchaseTriggerRepository) {
        this.valueRepository = valueRepository;
        this.purchaseTriggerRepository = purchaseTriggerRepository;
    }

    public static void main(String[] args) {
        SpringApplication.run(DonkeyKongTransactionApplication.class, args);
    }

    @Override
    public void run(String... args) {
        valueRepository.deleteAll();
        purchaseTriggerRepository.deleteAll();
    }
}
