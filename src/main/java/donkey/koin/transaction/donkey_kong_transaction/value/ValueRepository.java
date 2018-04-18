package donkey.koin.transaction.donkey_kong_transaction.value;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Date;
import java.util.List;

public interface ValueRepository extends MongoRepository<Value, String> {
    List<Value> findByDate(Date date);
}
