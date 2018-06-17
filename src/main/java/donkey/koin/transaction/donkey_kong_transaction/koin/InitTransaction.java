package donkey.koin.transaction.donkey_kong_transaction.koin;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class InitTransaction {

    private final byte[] publicKey;
    private final double moneyAmount;

    public InitTransaction(@JsonProperty("moneyAmount") Double moneyAmount,
                           @JsonProperty("publicKey") byte[] publicKey) {
        this.moneyAmount = moneyAmount;
        this.publicKey = publicKey;
    }

}
