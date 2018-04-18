package donkey.koin.transaction.donkey_kong_transaction.value;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;

import java.util.Date;

@Data
@RequiredArgsConstructor
public class Value {
    private final Date date;
    private final int cents;
    @Id
    private String id;
}
