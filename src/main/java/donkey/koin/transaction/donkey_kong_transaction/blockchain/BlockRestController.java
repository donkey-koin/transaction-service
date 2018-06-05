package donkey.koin.transaction.donkey_kong_transaction.blockchain;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/blocks")
public class BlockRestController {

    private final BlockService blockService;

    public BlockRestController(BlockService blockService) {
        this.blockService = blockService;
    }

    @PostMapping
    public void addBlock(@RequestBody Data data) {
        blockService.addBlock(data.getData());
    }
}