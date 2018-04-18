package donkey.koin.transaction.donkey_kong_transaction.value;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
public class ValueController {
    //    private Value value;
//
    @RequestMapping(value = "/value", method = RequestMethod.GET)
    @ResponseBody
    public Value getValue() {
        return new Value(new Date(), 100);
    }
}
