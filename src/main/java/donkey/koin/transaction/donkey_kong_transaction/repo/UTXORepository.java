package donkey.koin.transaction.donkey_kong_transaction.repo;

import donkey.koin.transaction.donkey_kong_transaction.entities.UTXO;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface UTXORepository extends MongoRepository<UTXO, String> {
    Optional<UTXO> findByTxHash(byte[] txHash);

    void deleteByTxHash(byte[] txHash);

    List<UTXO> findAllByAddressEquals(byte[] adress);
}
