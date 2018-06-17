package donkey.koin.transaction.donkey_kong_transaction.crypto;

import donkey.koin.transaction.donkey_kong_transaction.koin.KoinManager;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static donkey.koin.transaction.donkey_kong_transaction.crypto.TransactionApplyController.TRANSACTION_ENDPOINT;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@RequestMapping(TRANSACTION_ENDPOINT)
public class TransactionApplyController {

    public static final String TRANSACTION_ENDPOINT = "/transaction";

    private final KoinManager koinManager;

    public TransactionApplyController(KoinManager koinManager) {
        this.koinManager = koinManager;
    }

    @RequestMapping(method = POST)
    public void potentiallyPersistTransaction(@RequestBody PotentialTransaction potentialTransaction) {
        koinManager.addTransaction(potentialTransaction.getOutputsMap(), potentialTransaction.getRecipientPublicKey(),
                potentialTransaction.getAmount());
    }
}
