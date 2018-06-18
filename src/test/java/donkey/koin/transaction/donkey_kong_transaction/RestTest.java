package donkey.koin.transaction.donkey_kong_transaction;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import donkey.koin.transaction.donkey_kong_transaction.crypto.MiniTransaction;
import donkey.koin.transaction.donkey_kong_transaction.crypto.PotentialTransaction;
import donkey.koin.transaction.donkey_kong_transaction.entities.UTXO;
import donkey.koin.transaction.donkey_kong_transaction.koin.KoinManager;
import donkey.koin.transaction.donkey_kong_transaction.repo.TransactionRepository;
import donkey.koin.transaction.donkey_kong_transaction.repo.UTXORepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static donkey.koin.transaction.donkey_kong_transaction.crypto.TransactionApplyController.TRANSACTION_ENDPOINT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = DonkeyKongTransactionApplication.class)
@AutoConfigureMockMvc
@DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)
public class RestTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UTXORepository utxoRepository;

    @Autowired
    private KoinManager koinManager;

    @Autowired
    TransactionRepository transactionRepository;

    @Before
    public void cleanDatabase() {
        utxoRepository.deleteAll();
        transactionRepository.deleteAll();
        koinManager.createInitialTransaction();
    }

    @Test
    public void rest_test() throws Exception {
        //given
        double valueToTakeFromManager = 3;
        double valueManagerHasAfterExtraction = 1000 - valueToTakeFromManager;

        byte[] recipientPublicKey = TestUtil.generateKeyPair().getPublic().getEncoded();
        byte[] koinManagerPublicKey = koinManager.getKeyPair().getPublic().getEncoded();
        MiniTransaction miniTransaction = new MiniTransaction(koinManagerPublicKey, valueToTakeFromManager);

        List<MiniTransaction> usersToTakeMoneyFromToAmount = Arrays.asList(miniTransaction);
        PotentialTransaction potentialTransaction = new PotentialTransaction(usersToTakeMoneyFromToAmount, recipientPublicKey, valueToTakeFromManager, 0);

        Gson gsonBuilder = new GsonBuilder().setPrettyPrinting().create();
        String json = gsonBuilder.toJson(potentialTransaction);

        //when
        mockMvc.perform(post(TRANSACTION_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().is2xxSuccessful());

        List<UTXO> utxos = utxoRepository.findAll();

        //then
        assertThat(utxos)
                .extracting("address", "value")
                .containsExactlyInAnyOrder(tuple(koinManagerPublicKey, valueManagerHasAfterExtraction), tuple(recipientPublicKey, valueToTakeFromManager));
    }
}
