package donkey.koin.transaction.donkey_kong_transaction.blockchain;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BlockRepository extends MongoRepository<Block, String> {
    List<Block> findAllByOrderByOrder();
    Block findFirstByOrderByOrder();
}