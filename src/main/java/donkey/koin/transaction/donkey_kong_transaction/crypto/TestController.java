package donkey.koin.transaction.donkey_kong_transaction.crypto;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
public class TestController {

    @Transactional
    @RequestMapping(path = "/test", method = POST)
    public void testPut(@RequestBody Map<Double, Double> map) {
    }
}
