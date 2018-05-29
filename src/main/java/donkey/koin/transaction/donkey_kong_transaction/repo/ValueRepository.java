package donkey.koin.transaction.donkey_kong_transaction.repo;

import donkey.koin.transaction.donkey_kong_transaction.entities.Value;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.lang.Nullable;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface ValueRepository extends MongoRepository<Value, String> {  //, CrudRepository<Value,String> {
    @Nullable
    List<Value> findAllByDate(Instant date);

    Optional<Value> findById(String id);

    List<Value> findAllByDateLessThanEqualOrderByDateDesc(Instant date);
}
