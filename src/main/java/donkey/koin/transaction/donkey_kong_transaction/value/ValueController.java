package donkey.koin.transaction.donkey_kong_transaction.value;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class ValueController {
    @Resource
    private Value value;

    @RequestMapping(value = "/value", method = RequestMethod.GET)
    @ResponseBody
    public Value getValue() {
        return value;
    }
}
