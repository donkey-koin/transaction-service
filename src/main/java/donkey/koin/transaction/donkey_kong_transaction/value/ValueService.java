package donkey.koin.transaction.donkey_kong_transaction.value;

import donkey.koin.transaction.donkey_kong_transaction.entities.Value;
import donkey.koin.transaction.donkey_kong_transaction.repo.ValueRepository;
import donkey.koin.transaction.donkey_kong_transaction.utils.TimeManagement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

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

    public List<Value> findLastValues(Instant instant, int lastN) {
        try {
            return valueRepository.findAllByDateLessThanEqualOrderByDateDesc(instant).subList(0, lastN);
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }
}
