package donkey.koin.transaction.donkey_kong_transaction.blockchain;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/blocks")
public class BlockRestController {

    private final BlockService blockService;

    public BlockRestController(BlockService blockService) {
        this.blockService = blockService;
    }

    @RequestMapping(method = RequestMethod.POST)
    public void addBlock(@RequestBody Data data) {
        blockService.addBlock(data.getData());
    }
}