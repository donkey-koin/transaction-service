package donkey.koin.transaction.donkey_kong_transaction.values.value;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;

import java.util.Date;

@Data
@RequiredArgsConstructor
public class Value {
    @Id
    private String id;
    private final Date date;
    private final int cents;
}
