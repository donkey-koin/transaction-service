package donkey.koin.transaction.donkey_kong_transaction.values.value;


import donkey.koin.transaction.donkey_kong_transaction.utils.TimeManagement;
import donkey.koin.transaction.donkey_kong_transaction.values.ValueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/values")
public class ValueController {

    private final ValueRepository repository;

    @Autowired
    public ValueController(ValueRepository repository) {
        this.repository = repository;
    }

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    List<Value> getValue() {
        return this.repository.findAll();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{valueId}")
    Optional<Value> readValue(@PathVariable String valueId) {
        return this.repository.findById(valueId);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/search")
    public List<Value> findValuesByDate(@RequestParam("date") String date) {
        System.out.println(date);
        Instant instant;

        try {
            instant = Instant.parse(date);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            instant = TimeManagement.deleteNano(Instant.now());
        }

        return this.repository.findAllByDate(instant);
    }

}
