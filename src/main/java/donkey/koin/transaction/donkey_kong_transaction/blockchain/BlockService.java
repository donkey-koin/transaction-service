package donkey.koin.transaction.donkey_kong_transaction.blockchain;

import donkey.koin.transaction.donkey_kong_transaction.utils.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Service
@Slf4j
public class BlockService {

    private final BlockRepository blockRepository;
    private AtomicLong atomicLong;

    public BlockService(BlockRepository blockRepository) {
        this.blockRepository = blockRepository;
    }

    @PostConstruct
    public void init() {
        long count = blockRepository.count();
        this.atomicLong = new AtomicLong(count);
        if (count == 0) {
            Block block = new Block(0, "First block", "0");
            block.setHash("0");
            blockRepository.insert(block);
        }
    }

    public Boolean isChainValid(List<Block> blockchain) {
        Block currentBlock;
        Block previousBlock;
        //loop through blockchain to check hashes:
        for (int i = 1; i < blockchain.size(); i++) {
            currentBlock = blockchain.get(i);
            previousBlock = blockchain.get(i - 1);
            //compare registered hash and calculated hash:
            if (!currentBlock.getHash().equals(calculateHash(previousBlock))) {
                log.info("Current Hashes not equal");
                return false;
            }
            //compare previous hash and registered previous hash
            if (!previousBlock.getHash().equals(currentBlock.getPreviousHash())) {
                log.info("Previous Hashes not equal");
                return false;
            }
        }
        return true;
    }

    public String calculateHash(Block block) {
        return StringUtil.applySha256(block.getPreviousHash() + Long.toString(block.getTimeStamp()) + block.getData());
    }

    public Block addBlock(String data) {
        Block latestBlock = blockRepository.findFirstByOrderByOrderDesc();
        Block block = new Block(atomicLong.incrementAndGet(), data, latestBlock.getHash());
        block.setHash(calculateHash(latestBlock));

        List<Block> blockchain = blockRepository.findAllByOrderByOrder();
        Boolean chainValid = isChainValid(blockchain);

        return blockRepository.insert(block);
    }
}