package donkey.koin.transaction.donkey_kong_transaction.repo;

import donkey.koin.transaction.donkey_kong_transaction.crypto.Transaction;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends MongoRepository<Transaction, String> {

}
