package donkey.koin.transaction.donkey_kong_transaction.inprogres;

import donkey.koin.transaction.donkey_kong_transaction.repo.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
public class TestController {

    @Autowired
    private TransactionRepository transactionRepository;

    @Transactional
    @RequestMapping(path = "/test", method = POST)
    public void testPut() throws NoSuchAlgorithmException {
        Transaction t = new Transaction();
        t.setHash("test".getBytes());
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(512);
        Transaction.Input i = new Transaction.Input("previous".getBytes(), 0);
        KeyPair keyPair = generator.generateKeyPair();
        Transaction.Output o = new Transaction.Output(5, keyPair.getPublic().getEncoded());
        ArrayList<Transaction.Input> inputs = new ArrayList<>();
        inputs.add(i);
        ArrayList<Transaction.Output> outputs = new ArrayList<>();
        outputs.add(o);
        t.setInputs(inputs);
        t.setOutputs(outputs);
        t.calculateHash();
        transactionRepository.insert(t);
    }
}
