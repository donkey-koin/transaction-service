package donkey.koin.transaction.donkey_kong_transaction.koin;


import donkey.koin.transaction.donkey_kong_transaction.crypto.Transaction;
import donkey.koin.transaction.donkey_kong_transaction.entities.UTXO;
import donkey.koin.transaction.donkey_kong_transaction.repo.TransactionRepository;
import donkey.koin.transaction.donkey_kong_transaction.repo.UTXORepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static java.util.Comparator.comparing;

@Component
public class KoinManager {

    @Autowired
    UTXORepository utxoRepository;

    @Autowired
    TransactionRepository transactionRepository;

    private KeyPair keyPair;

    @Value("${donkey.koin.initial.value}")
    private double initialAmount;

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
        transactionRepository.deleteAll();
        utxoRepository.deleteAll();
        Transaction t = new Transaction();
        Transaction.Output o = new Transaction.Output(initialAmount, keyPair.getPublic().getEncoded());
        ArrayList<Transaction.Input> inputs = new ArrayList<>();
        ArrayList<Transaction.Output> outputs = new ArrayList<>();
        outputs.add(o);
        t.setInputs(inputs);
        t.setOutputs(outputs);
        t.calculateHash();
        transactionRepository.save(t);

        UTXO utxo = new UTXO(t.getHash(), 0);
        utxo.setAddress(keyPair.getPublic().getEncoded());
        utxo.setValue(o.getValue());
        utxoRepository.save(utxo);

        RestTemplate restTemplate = new RestTemplate();
        String orchUrl = "http://localhost:5000/init";
        HttpEntity<InitTransaction> request;
        HttpHeaders headers;
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        request = new HttpEntity<>(new InitTransaction(initialAmount, keyPair.getPublic().getEncoded()), headers);

        restTemplate.exchange(orchUrl, HttpMethod.POST, request, InitTransaction.class);

    }

    @Transactional
    public List<WalletUpdateAction> addTransaction(Map<PublicKey, Double> owners, PublicKey recipient, double coinAmount, double lastKoinValue) {
        List<WalletUpdateAction> walletUpdateActions = new ArrayList<>();

        List<UTXO> utxosToUtilize = new LinkedList<>();
        Transaction transaction = new Transaction();
        for (PublicKey publicKey : owners.keySet()) {

            List<UTXO> ownerUtxos = utxoRepository.findAllByAddressEquals(publicKey.getEncoded());
            Double ownerCoins = 0d;

            WalletUpdateAction walletUpdateAction = new WalletUpdateAction(publicKey.getEncoded(), -owners.get(publicKey), lastKoinValue);
            walletUpdateActions.add(walletUpdateAction);

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

        for (UTXO utxo : utxosToUtilize) {
            Transaction.Input input = new Transaction.Input(utxo.getTxHash(), utxo.getIndex());
            input.setSignature(utxo.getAddress());
            transaction.addInput(input);
        }

        transaction.addOutput(coinAmount, recipient);
        transaction.calculateHash();
        transaction.setPreviousHash(getPreviousTransactionHash());

        transactionRepository.save(transaction);

        List<UTXO> newUtxos = createNewUtxos(transaction);

        utxoRepository.saveAll(newUtxos);

        WalletUpdateAction buyerWalletUpdateAction = new WalletUpdateAction(recipient.getEncoded(), coinAmount, lastKoinValue);
        walletUpdateActions.add(buyerWalletUpdateAction);

        return walletUpdateActions;
    }

    @Transactional
    public List<WalletUpdateAction> sellTransaction(Map<PublicKey, Double> owners, PublicKey seller, double coinAmount, double lastKoinValue) {
        List<WalletUpdateAction> walletUpdateActions = new ArrayList<>();

        List<UTXO> utxosToUtilize = new LinkedList<>();
        Transaction transaction = new Transaction();
        List<UTXO> allSellersUtxos = utxoRepository.findAllByAddressEquals(seller.getEncoded());

        double amountAfterUtxoUtilisation = coinAmount;

        for (UTXO sellerUtxo : allSellersUtxos) {
            double value = sellerUtxo.getValue();
            utxosToUtilize.add(sellerUtxo);
            amountAfterUtxoUtilisation -= value;

            if (amountAfterUtxoUtilisation <= 0) {
                if (amountAfterUtxoUtilisation < 0) {
                    transaction.addOutput(Math.abs(amountAfterUtxoUtilisation), seller);
                }
                break;
            }
        }

        utxoRepository.deleteAll(utxosToUtilize);

        for (UTXO utxo : utxosToUtilize) {
            Transaction.Input input = new Transaction.Input(utxo.getTxHash(), utxo.getIndex());
            input.setSignature(utxo.getAddress());
            transaction.addInput(input);
        }

        owners.forEach((buyerKey, amountToOutput) -> {
            transaction.addOutput(amountToOutput, buyerKey);
            walletUpdateActions.add(new WalletUpdateAction(buyerKey.getEncoded(), amountToOutput, lastKoinValue));
        });

        transaction.calculateHash();
        transaction.setPreviousHash(getPreviousTransactionHash());

        List<UTXO> newUtxos = createNewUtxos(transaction);
        utxoRepository.saveAll(newUtxos);

        WalletUpdateAction buyerWalletUpdateAction = new WalletUpdateAction(seller.getEncoded(), -coinAmount, lastKoinValue);
        walletUpdateActions.add(buyerWalletUpdateAction);

        return walletUpdateActions;
    }

    private byte[] getPreviousTransactionHash() {
        return transactionRepository.findAll().stream()
                .max(comparing(Transaction::getTimestamp))
                .map(Transaction::getHash)
                .orElse(new byte[]{0});
    }

    private List<UTXO> createNewUtxos(Transaction transaction) {
        AtomicInteger outputIndex = new AtomicInteger(0);
        return transaction.getOutputs().stream()
                .map(output -> {
                    UTXO utxo = new UTXO(transaction.getHash(), outputIndex.getAndIncrement());
                    utxo.setValue(output.getValue());
                    utxo.setAddress(output.getAddress());
                    return utxo;
                })
                .collect(Collectors.toList());
    }

    public KeyPair getKeyPair() {
        return keyPair;
    }
}
