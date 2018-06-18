package donkey.koin.transaction.donkey_kong_transaction.crypto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.security.PublicKey;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class PotentialTransaction {
    private List<MiniTransaction> usersToTakeMoneyFromToAmount;
    private byte[] recipient;
    private double amount;
    private double lastKoinValue;

    public PotentialTransaction(@JsonProperty("usersToTakeMoneyFromToAmount") List<MiniTransaction> usersToTakeMoneyFromToAmount,
                                @JsonProperty("recipient") byte[] recipient,
                                @JsonProperty("amount") double amount,
                                @JsonProperty("lastKoinValue") double lastKoinValue) {
        this.usersToTakeMoneyFromToAmount = usersToTakeMoneyFromToAmount;
        this.recipient = recipient;
        this.amount = amount;
        this.lastKoinValue = lastKoinValue;
    }

    @JsonIgnore
    public PublicKey getRecipientPublicKey() {
        return TxHandler.getRsaPublicKeyKeyFromBytes(recipient);
    }

    @JsonIgnore
    public Map<PublicKey, Double> getOutputsMap() {
        Map<PublicKey, Double> outputs = new HashMap<>();
        usersToTakeMoneyFromToAmount.forEach((transaction) ->
                outputs.put(transaction.getPublicKey(), transaction.getAmount()));
        return outputs;
    }
}
