package donkey.koin.transaction.donkey_kong_transaction.koin;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class WalletUpdateAction {

    private final byte[] publicKey;
    private double donkeyKoin;

    public WalletUpdateAction(@JsonProperty("publicKey") byte[] publicKey,
                              @JsonProperty("donkeyKoin") double donkeyKoin) {
        this.publicKey = publicKey;
        this.donkeyKoin = donkeyKoin;
    }

    public void addAmount(double donkeyKoin) {
        this.donkeyKoin += donkeyKoin;
    }

//    private final double euroAmount;
}
