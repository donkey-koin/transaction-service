package donkey.koin.transaction.donkey_kong_transaction;

import java.security.KeyPair;
import java.security.KeyPairGenerator;

public class TestUtil {

    public static KeyPair generateKeyPair() {
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
