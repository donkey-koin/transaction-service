package donkey.koin.transaction.donkey_kong_transaction.crypto;

import donkey.koin.transaction.donkey_kong_transaction.koin.KoinManager;
import donkey.koin.transaction.donkey_kong_transaction.repo.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static donkey.koin.transaction.donkey_kong_transaction.crypto.TransactionApplyController.TRANSACTION_ENDPOINT;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@RequestMapping(TRANSACTION_ENDPOINT)
public class TransactionApplyController {

    public static final String TRANSACTION_ENDPOINT = "/transaction";

    private final KoinManager koinManager;

    @Autowired
    private final TransactionRepository transactionRepository;

    public TransactionApplyController(KoinManager koinManager, TransactionRepository transactionRepository) {
        this.koinManager = koinManager;
        this.transactionRepository = transactionRepository;
    }

    @RequestMapping(method = POST)
    public void potentiallyPersistTransaction(@RequestBody PotentialTransaction potentialTransaction) {
        koinManager.addTransaction(potentialTransaction.getOutputsMap(), potentialTransaction.getRecipientPublicKey(),
                potentialTransaction.getAmount());
    }

    @RequestMapping(method = GET)
    List<Transaction> getAllTransactions() {
        List<Transaction> transactions = this.transactionRepository.findAll();
        System.out.println(transactions);
        return transactions;
    }

    @RequestMapping(method = GET, value = "/{publicKey}")
    List<Transaction> getMyTransactions(@PathVariable byte[] publicKey) {
        List<Transaction> transactions = this.transactionRepository.findAll();
        List<Transaction> myTransactions = new ArrayList<>();
        for (Transaction transaction : transactions) {
            for (Transaction.Output output : transaction.getOutputs()) {
                if (Arrays.equals(output.getAddress(), publicKey)) {
                    myTransactions.add(transaction);
                    break;
                }
            }
        }
        return myTransactions;
    }

}
