package donkey.koin.transaction.donkey_kong_transaction.repo;

import donkey.koin.transaction.donkey_kong_transaction.entities.PurchaseTrigger;
import donkey.koin.transaction.donkey_kong_transaction.purchase.trigger.PurchaseTriggerAction;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface PurchaseTriggerRepository extends MongoRepository<PurchaseTrigger, String> {

    List<PurchaseTrigger> findByLimitGreaterThanEqualAndActionEquals(double value, PurchaseTriggerAction trigger);

    List<PurchaseTrigger> findByLimitLessThanEqualAndActionEquals(double value, PurchaseTriggerAction trigger);

    List<PurchaseTrigger> findAllByUsernameEquals(String username);
}
