package donkey.koin.transaction.donkey_kong_transaction.value;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.Date;

@Data
@Component
public class Value {
    private Date date;
    private int cents;
}
