package donkey.koin.transaction.donkey_kong_transaction.values;

import donkey.koin.transaction.donkey_kong_transaction.values.value.Value;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface ValueRepository extends MongoRepository<Value, String> {
    List<Value> findByDate(Date date);

    Optional<Value> findById(String id);
}
