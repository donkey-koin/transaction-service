package donkey.koin.transaction.donkey_kong_transaction.values;

import donkey.koin.transaction.donkey_kong_transaction.utils.TimeManagement;
import donkey.koin.transaction.donkey_kong_transaction.values.value.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class ValueService {

    @Autowired
    private ValueRepository valueRepository;

    public Instant getInstantFromString(String date) {
        Instant instant;

        try {
            instant = Instant.parse(date);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            instant = TimeManagement.deleteNano(Instant.now());
        }

        return instant;
    }

    public Value findNewestByDate(Instant instant) {
        return valueRepository.findAllByDateLessThanEqualOrderByDateDesc(instant).get(0);
    }

}
