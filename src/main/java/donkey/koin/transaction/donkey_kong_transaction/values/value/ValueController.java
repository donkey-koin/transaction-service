package donkey.koin.transaction.donkey_kong_transaction.values.value;


import donkey.koin.transaction.donkey_kong_transaction.values.ValueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
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
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'THH:mm");
        LocalDateTime dateTime;// = LocalDateTime.parse(date, formatter);
        try {
            Instant instant = Instant.parse(date);
            dateTime = LocalDateTime.ofInstant(instant, ZoneOffset.UTC);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            dateTime = LocalDateTime.now();
        }

        System.out.println(dateTime);
        return this.repository.findAllByDate(dateTime);
    }

}
