package donkey.koin.transaction.donkey_kong_transaction.entities;


import donkey.koin.transaction.donkey_kong_transaction.purchase.trigger.PurchaseTriggerAction;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Data
@Document
@RequiredArgsConstructor
public class PurchaseTrigger {
    private final String token;
    private final int value;
    private final int coinAmount;
    private final PurchaseTriggerAction action;

    @Id
    private String id;

}
