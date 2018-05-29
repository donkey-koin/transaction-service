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
    public void add(@RequestBody PurchaseTriggerDto purchaseTrigger) {
        PurchaseTrigger trigger = new PurchaseTrigger(purchaseTrigger.getToken(),
                purchaseTrigger.getValue(), purchaseTrigger.getCoinAmount(), purchaseTrigger.getAction());
//        System.out.println(n);

        this.repository.save(trigger);
        System.out.println(trigger);
    }


}
