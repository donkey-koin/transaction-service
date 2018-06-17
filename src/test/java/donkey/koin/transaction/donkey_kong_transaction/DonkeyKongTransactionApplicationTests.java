package donkey.koin.transaction.donkey_kong_transaction;

import donkey.koin.transaction.donkey_kong_transaction.crypto.Transaction;
import donkey.koin.transaction.donkey_kong_transaction.entities.UTXO;
import donkey.koin.transaction.donkey_kong_transaction.koin.KoinManager;
import donkey.koin.transaction.donkey_kong_transaction.repo.TransactionRepository;
import donkey.koin.transaction.donkey_kong_transaction.repo.UTXORepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.security.KeyPair;
import java.security.PublicKey;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD;

@RunWith(SpringRunner.class)
@SpringBootTest
@DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)
public class DonkeyKongTransactionApplicationTests {

    @Autowired
    UTXORepository utxoRepository;

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    KoinManager koinManager;

    @Before
    public void cleanDatabase() {
        utxoRepository.deleteAll();
        transactionRepository.deleteAll();
    }

    @Test
    public void generates_first_transaction_on_init() {
        // given
        koinManager.createInitialTransaction();

        // when
        List<Transaction> transactions = transactionRepository.findAll();

        // then
        assert transactions.size() == 1;
        assert transactions.get(0).getOutputs().size() == 1;
        assert transactions.get(0).getOutputs().get(0).value == 1000d;
    }

    @Test
    public void adds_correct_transaction() {
        // given
        koinManager.createInitialTransaction();
        Map<PublicKey, Double> map = new HashMap<>();
        KeyPair keyPair1 = TestUtil.generateKeyPair();
        map.put(koinManager.getKeyPair().getPublic(), 4.56d);

        // when
        koinManager.addTransaction(map, keyPair1.getPublic(), 4.56d);
        List<Transaction> transactions = transactionRepository.findAll();

        // then
        assert transactions.size() == 2;
        assert transactions.get(1).getOutputs().size() == 2;
        assert transactions.get(1).getOutputs().get(0).value == 995.44d;
        assert transactions.get(1).getOutputs().get(1).value == 4.56d;
    }

    @Test
    public void adds_correct_multiple_transactions() {
        // given
        koinManager.createInitialTransaction();
        Map<PublicKey, Double> map1 = new HashMap<>();
        Map<PublicKey, Double> map2 = new HashMap<>();
        KeyPair keyPair1 = TestUtil.generateKeyPair();
        KeyPair keyPair2 = TestUtil.generateKeyPair();

        map1.put(koinManager.getKeyPair().getPublic(), 500d);
        map2.put(keyPair1.getPublic(), 400d);
        map2.put(koinManager.getKeyPair().getPublic(), 200d);

        // when
        koinManager.addTransaction(map1, keyPair1.getPublic(), 500d);
        koinManager.addTransaction(map2, keyPair2.getPublic(), 600d);
        List<Transaction> transactions = transactionRepository.findAll();

        // then
        assert transactions.size() == 3;
        assert transactions.get(2).getOutputs().size() == 3;
        assert transactions.get(2).getOutputs().get(0).value == 100d;
        assert transactions.get(2).getOutputs().get(1).value == 300d;
        assert transactions.get(2).getOutputs().get(2).value == 600d;
    }

    @Test
    public void cleans_utxo_pool() {
        // given
        koinManager.createInitialTransaction();
        Map<PublicKey, Double> map = new HashMap<>();
        KeyPair keyPair1 = TestUtil.generateKeyPair();
        map.put(koinManager.getKeyPair().getPublic(), 4.56d);

        // when
        koinManager.addTransaction(map, keyPair1.getPublic(), 4.56d);
        List<UTXO> utxos = utxoRepository.findAll();

        // then
        assert utxos.size() == 2;
        assert utxos.get(0).getValue() == 995.44d;
        assert utxos.get(1).getValue() == 4.56d;
    }
}
