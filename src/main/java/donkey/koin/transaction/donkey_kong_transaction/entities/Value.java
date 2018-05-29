package donkey.koin.transaction.donkey_kong_transaction.entities;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Data
@RequiredArgsConstructor
@Document
public class Value {
    @Id
    private String id;
    private final Instant date;
    private final int cents;
}
