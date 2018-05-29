package donkey.koin.transaction.donkey_kong_transaction.repo;

import donkey.koin.transaction.donkey_kong_transaction.entities.PurchaseTrigger;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface PurchaseTriggerRepository extends MongoRepository<PurchaseTrigger, String> {

    List<PurchaseTrigger> findByValueGreaterThan(int value);

    List<PurchaseTrigger> findByValueLessThan(int value);

}