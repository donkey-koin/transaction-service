package donkey.koin.transaction.donkey_kong_transaction.repo;

import donkey.koin.transaction.donkey_kong_transaction.entities.PurchaseTrigger;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PurchaseTriggerRepository extends MongoRepository<PurchaseTrigger, String> {

}
