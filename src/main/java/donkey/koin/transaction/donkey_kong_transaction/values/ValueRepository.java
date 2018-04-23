package donkey.koin.transaction.donkey_kong_transaction.values;

import donkey.koin.transaction.donkey_kong_transaction.values.value.Value;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.lang.Nullable;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface ValueRepository extends MongoRepository<Value, String> {  //, CrudRepository<Value,String> {
    @Nullable
    List<Value> findAllByDate(Instant date);

    Optional<Value> findById(String id);
}
