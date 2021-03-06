package donkey.koin.transaction.donkey_kong_transaction.crypto;

import donkey.koin.transaction.donkey_kong_transaction.koin.KoinManager;
import donkey.koin.transaction.donkey_kong_transaction.koin.WalletUpdateAction;
import donkey.koin.transaction.donkey_kong_transaction.repo.TransactionRepository;
import org.apache.commons.lang.ArrayUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

import static donkey.koin.transaction.donkey_kong_transaction.crypto.TransactionApplyController.TRANSACTION_ENDPOINT;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@RequestMapping(TRANSACTION_ENDPOINT)
public class TransactionApplyController {

    public static final String TRANSACTION_ENDPOINT = "/transaction";

    private final KoinManager koinManager;

    private final TransactionRepository transactionRepository;

    public TransactionApplyController(KoinManager koinManager,
                                      TransactionRepository transactionRepository) {
        this.koinManager = koinManager;
        this.transactionRepository = transactionRepository;
    }

    @RequestMapping(method = POST)
    public ResponseEntity potentiallyPersistTransaction(@RequestBody PotentialTransaction potentialTransaction) {
        List<WalletUpdateAction> walletUpdateActions = koinManager.addTransaction(potentialTransaction.getOutputsMap(), potentialTransaction.getRecipientPublicKey(),
                potentialTransaction.getAmount(), potentialTransaction.getLastKoinValue());

        return ResponseEntity.ok(walletUpdateActions);
    }

    @RequestMapping(method = POST, value = "/sell")
    public ResponseEntity sellTransaction(@RequestBody PotentialTransaction potentialTransaction) {
        List<WalletUpdateAction> walletUpdateActions = koinManager.sellTransaction(potentialTransaction.getOutputsMap(), potentialTransaction.getRecipientPublicKey(),
                potentialTransaction.getAmount(), potentialTransaction.getLastKoinValue());

        return ResponseEntity.ok(walletUpdateActions);
    }

    @RequestMapping(method = GET)
    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    @RequestMapping(method = POST, path = "/find")
    public Set<Transaction> getMyTransactions(@RequestBody Map<String, Byte[]> bytes) {
        byte[] publicKey = ArrayUtils.toPrimitive(bytes.get("publicKey"));

        Set<Transaction> transactions = new LinkedHashSet<>(transactionRepository.findAll());
        Set<Transaction> myTransactions = new LinkedHashSet<>();

        for (Transaction transaction : transactions) {
            for (Transaction.Output output : transaction.getOutputs()) {
                if (Arrays.equals(output.getAddress(), publicKey)) {
                    myTransactions.add(transaction);
                    break;
                }
            }
            for (Transaction.Input input : transaction.getInputs()) {
                if (Arrays.equals(input.getSignature(), publicKey)) {
                    myTransactions.add(transaction);
                    break;
                }
            }
        }
        return myTransactions;
    }
}
