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

    @JsonProperty("limit")
    private double limit;

    @JsonProperty("coinAmount")
    private double coinAmount;

    @JsonProperty("action")
    private PurchaseTriggerAction action;

    @JsonProperty
    private String username;

    public static PurchaseTriggerDto fromPurchaseTrigger(PurchaseTrigger purchaseTrigger) {
        PurchaseTriggerDto dto = new PurchaseTriggerDto();
        dto.setToken(purchaseTrigger.getToken());
        dto.setCoinAmount(purchaseTrigger.getCoinAmount());
        dto.setLimit(purchaseTrigger.getLimit());
        dto.setAction(purchaseTrigger.getAction());
        dto.setUsername(purchaseTrigger.getUsername());
        return dto;
    }
}
