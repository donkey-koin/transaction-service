package donkey.koin.transaction.donkey_kong_transaction.blockchain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class Data {
    private final String data;

    public Data(@JsonProperty("data") String data) {
        this.data = data;
    }
}