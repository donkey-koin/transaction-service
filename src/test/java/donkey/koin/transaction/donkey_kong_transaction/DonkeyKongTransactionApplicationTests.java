package donkey.koin.transaction.donkey_kong_transaction;

import donkey.koin.transaction.donkey_kong_transaction.entities.UTXO;
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

@RunWith(SpringRunner.class)
@SpringBootTest
public class DonkeyKongTransactionApplicationTests {

    @Autowired
    UTXORepository utxoRepository;

    @Test
    public void contextLoads() {
        KeyPair keyPair;
        try {
            KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
            kpg.initialize(512);
            keyPair = kpg.generateKeyPair();
        } catch (Exception e) {
            e.printStackTrace();
            keyPair = null;
        }

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

        UTXO utxoxd = utxoRepository.findByAddressEquals(key1.getEncoded()).get();

        assert true;

    }

}
