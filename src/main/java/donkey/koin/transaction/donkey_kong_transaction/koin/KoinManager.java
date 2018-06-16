package donkey.koin.transaction.donkey_kong_transaction.koin;


import donkey.koin.transaction.donkey_kong_transaction.entities.UTXO;
import donkey.koin.transaction.donkey_kong_transaction.inprogres.Transaction;
import donkey.koin.transaction.donkey_kong_transaction.repo.TransactionRepository;
import donkey.koin.transaction.donkey_kong_transaction.repo.UTXORepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Component
public class KoinManager {

    @Autowired
    UTXORepository utxoRepository;

    @Autowired
    TransactionRepository transactionRepository;

    private KeyPair keyPair;
    private List<Transaction> allTransactions = new ArrayList<>();

    @Value("${donkey.koin.initial.value}")
    private int initialAmount;

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

    @PostConstruct
    public void createInitialTransaction() {
        Transaction t = new Transaction();
        Transaction.Output o = new Transaction.Output(initialAmount, keyPair.getPublic().getEncoded());
        ArrayList<Transaction.Input> inputs = new ArrayList<>();
        ArrayList<Transaction.Output> outputs = new ArrayList<>();
        outputs.add(o);
        t.setInputs(inputs);
        t.setOutputs(outputs);
        t.calculateHash();
        transactionRepository.save(t);

        UTXO utxo = new UTXO(t.getHash(),0);
        utxo.setAddress(keyPair.getPublic().getEncoded());
        utxo.setValue(o.getValue());
        utxoRepository.save(utxo);
    }

    @Transactional
    public void addTransaction(Map<PublicKey, Double> owners, PublicKey receipent, double coinAmount) {
        List<UTXO> utxosToUtilize = new LinkedList<>();
        Transaction transaction = new Transaction();
        for (PublicKey publicKey : owners.keySet()) {

            List<UTXO> ownerUtxos = utxoRepository.findAllByAddressEquals(publicKey.getEncoded());
            Double ownerCoins = 0d;

            for (UTXO utxo : ownerUtxos) {
                ownerCoins += utxo.getValue();
                utxosToUtilize.add(utxo);
                if (ownerCoins >= owners.get(publicKey)) {
                    if (ownerCoins > owners.get(publicKey)) {
                        transaction.addOutput(ownerCoins - owners.get(publicKey), publicKey);
                    }
                    break;
                }
            }
        }

        utxoRepository.deleteAll(utxosToUtilize);

        for (UTXO utxo:utxosToUtilize) {
            transaction.addInput(utxo.getTxHash(),utxo.getIndex());
        }

        transaction.addOutput(coinAmount,receipent);
        transaction.calculateHash();
        transactionRepository.save(transaction);

    }


    public KeyPair getKeyPair() {
        return keyPair;
    }
}
