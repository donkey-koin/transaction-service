package donkey.koin.transaction.donkey_kong_transaction;

import donkey.koin.transaction.donkey_kong_transaction.entities.UTXO;
import donkey.koin.transaction.donkey_kong_transaction.inprogres.Transaction;
import donkey.koin.transaction.donkey_kong_transaction.koin.KoinManager;
import donkey.koin.transaction.donkey_kong_transaction.repo.TransactionRepository;
import donkey.koin.transaction.donkey_kong_transaction.repo.UTXORepository;
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

    @Test
    public void contextLoads() {
        KeyPair keyPair = generateKeyPair();

        UTXO utxo = new UTXO("sada".getBytes(),1);

        PublicKey key1 = keyPair.getPublic();
        utxo.setAddress(key1.getEncoded());
        utxoRepository.save(utxo);
        byte[] bytes = key1.getEncoded();

        PublicKey publicKey;
        try {
            publicKey =
                    KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(bytes));
        } catch (Exception e) {

        }

//        UTXO utxoxd = utxoRepository.findByAddressEquals(key1.getEncoded()).get();

        assert true;

    }

    @Test
    public void generates_first_transaction_on_init() {
        Map<PublicKey,Double> map = new HashMap<>();
        KeyPair keyPair1 = generateKeyPair();
        map.put(koinManager.getKeyPair().getPublic(),4.56d);
        koinManager.addTransaction(map,keyPair1.getPublic(),4.56d);

        List<Transaction> t = transactionRepository.findAll();
        int i = 2;
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
