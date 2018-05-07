package donkey.koin.transaction.donkey_kong_transaction.values.value;


import donkey.koin.transaction.donkey_kong_transaction.values.ValueRepository;
import donkey.koin.transaction.donkey_kong_transaction.values.ValueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/values")
public class ValueController {

    private final ValueRepository repository;
    private final ValueService valueService;

    @Autowired
    public ValueController(ValueRepository repository, ValueService valueService) {
        this.repository = repository;
        this.valueService = valueService;
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
    public List<Value> findValuesByDate(@RequestParam("date") String date, @RequestParam("last") int last) {
        Instant instantFromString = valueService.getInstantFromString(date);
        return valueService.findLastValues(instantFromString, last);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/newestValue")
    public Value findNewestValueByDate(@RequestParam("date") String date) {
        Instant instantFromString = valueService.getInstantFromString(date);
        return valueService.findNewestByDate(instantFromString);
    }
}
