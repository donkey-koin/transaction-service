package donkey.koin.transaction.donkey_kong_transaction.purchase.trigger;

import com.fasterxml.jackson.annotation.JsonProperty;
import donkey.koin.transaction.donkey_kong_transaction.entities.PurchaseTrigger;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PurchaseTriggerDto {
    @JsonProperty("token")
    private String token;

    @JsonProperty("value")
    private int value;

    @JsonProperty("coinAmount")
    private int coinAmount;

    @JsonProperty("action")
    private PurchaseTriggerAction action;

    public static PurchaseTriggerDto fromPurchaseTrigger(PurchaseTrigger purchaseTrigger) {
        PurchaseTriggerDto dto = new PurchaseTriggerDto();
        dto.setToken(purchaseTrigger.getToken());
        dto.setValue(purchaseTrigger.getValue());
        dto.setCoinAmount(purchaseTrigger.getCoinAmount());
        dto.setAction(purchaseTrigger.getAction());
        return dto;
    }

}
