package donkey.koin.transaction.donkey_kong_transaction.purchase.trigger;


import donkey.koin.transaction.donkey_kong_transaction.entities.PurchaseTrigger;
import donkey.koin.transaction.donkey_kong_transaction.repo.PurchaseTriggerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/triggers")
public class PurchaseTriggerController {

    @Autowired
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
        System.out.println(trigger);
    }


}
