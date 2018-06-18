package donkey.koin.transaction.donkey_kong_transaction.purchase.trigger;


import donkey.koin.transaction.donkey_kong_transaction.entities.PurchaseTrigger;
import donkey.koin.transaction.donkey_kong_transaction.repo.PurchaseTriggerRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/triggers")
public class PurchaseTriggerController {

    private final PurchaseTriggerRepository repository;

    public PurchaseTriggerController(PurchaseTriggerRepository repository) {
        this.repository = repository;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public void add(@RequestBody PurchaseTriggerDto purchaseTrigger, @RequestHeader String authorization) {
        PurchaseTrigger trigger = new PurchaseTrigger(authorization, purchaseTrigger.getUsername(),
                purchaseTrigger.getLimit(), purchaseTrigger.getCoinAmount(), purchaseTrigger.getAction());

        this.repository.save(trigger);
        log.info(trigger.toString());
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{username}")
    public List<PurchaseTrigger> get(@PathVariable("username") String username) {
        return repository.findAllByUsernameEquals(username);
    }


}
