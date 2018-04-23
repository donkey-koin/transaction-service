package donkey.koin.transaction.donkey_kong_transaction.utils;

import java.time.Instant;

public class TimeManagement {
    public static Instant deleteNano(Instant instant) {
        return instant.minusNanos(instant.getNano());
    }

}
