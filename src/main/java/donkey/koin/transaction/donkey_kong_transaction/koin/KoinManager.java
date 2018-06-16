package donkey.koin.transaction.donkey_kong_transaction.koin;


import donkey.koin.transaction.donkey_kong_transaction.inprogres.Transaction;
import org.springframework.stereotype.Component;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.util.ArrayList;
import java.util.List;

@Component
public class KoinManager {

    private KeyPair keyPair;
    private List<Transaction> allTransactions = new ArrayList<>();

    public KoinManager() {
        try {
            KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
            kpg.initialize(512);
            keyPair = kpg.generateKeyPair();
        } catch (Exception e) {
            e.printStackTrace();
            keyPair = null;
        }
    }

    public void createInitialTransaction(Integer koinAmount) {
        Transaction t = new Transaction();
        Transaction.Output o = t.new Output(koinAmount, keyPair.getPublic());
        ArrayList<Transaction.Input> inputs = new ArrayList<>();
        ArrayList<Transaction.Output> outputs = new ArrayList<>();
        outputs.add(o);
        t.setInputs(inputs);
        t.setOutputs(outputs);
        t.calculateHash();
        allTransactions.add(t);
    }


}
