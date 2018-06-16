package donkey.koin.transaction.donkey_kong_transaction;

import donkey.koin.transaction.donkey_kong_transaction.entities.UTXO;
import donkey.koin.transaction.donkey_kong_transaction.inprogres.Transaction;
import donkey.koin.transaction.donkey_kong_transaction.koin.KoinManager;
import donkey.koin.transaction.donkey_kong_transaction.repo.TransactionRepository;
import donkey.koin.transaction.donkey_kong_transaction.repo.UTXORepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PublicKey;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
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
        KeyPair keyPair1 = generateKeyPair();
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

    private KeyPair generateKeyPair() {
        KeyPair keyPair;
        try {
            KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
            kpg.initialize(512);
            keyPair = kpg.generateKeyPair();
        } catch (Exception e) {
            e.printStackTrace();
            keyPair = null;
        }
        return keyPair;
    }

}
