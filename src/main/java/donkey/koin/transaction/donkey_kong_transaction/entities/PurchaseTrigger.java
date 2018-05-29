package donkey.koin.transaction.donkey_kong_transaction.entities;


import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashMap;
import java.util.Map;

@Data
@Document
@RequiredArgsConstructor
public class PurchaseTrigger {
    private final String token;
    private final Map<Integer, Integer> priceToCoinsMap = new HashMap<>();
    @Id
    private String id;

}
