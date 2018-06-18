package donkey.koin.transaction.donkey_kong_transaction.koin;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class WalletUpdateAction {

    private final byte[] publicKey;
    private final double lastKoinValue;
    private double donkeyKoin;

    public WalletUpdateAction(@JsonProperty("publicKey") byte[] publicKey,
                              @JsonProperty("donkeyKoin") double donkeyKoin,
                              @JsonProperty("lastKoinValue") double lastKoinValue) {
        this.publicKey = publicKey;
        this.donkeyKoin = donkeyKoin;
        this.lastKoinValue = lastKoinValue;
    }

    public void addAmount(double donkeyKoin) {
        this.donkeyKoin += donkeyKoin;
    }
}
