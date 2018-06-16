package donkey.koin.transaction.donkey_kong_transaction.inprogres;

import donkey.koin.transaction.donkey_kong_transaction.entities.UTXO;
import donkey.koin.transaction.donkey_kong_transaction.repo.UTXORepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

@Service
public class UTXOService {


    /**
     * The current collection of UTXOs, with each one mapped to its corresponding transaction output
     */
    private UTXORepository utxoRepository;

    public UTXOService(UTXORepository utxoRepository) {
        this.utxoRepository = utxoRepository;
    }

    /**
     * Adds a mapping from UTXO {@code utxo} to transaction output @code{txOut} to the pool
     */
    public void addUTXO(UTXO utxo) {
        utxoRepository.save(utxo);
    }

    /**
     * Removes the UTXO {@code utxo} from the pool
     */
    public void removeUTXO(UTXO utxo) {
        utxoRepository.delete(utxo);
    }


    /**
     * @return true if UTXO {@code utxo} is in the pool and false otherwise
     */
    public boolean contains(UTXO utxo) {
        return utxoRepository.findByTxHash(utxo.getTxHash()).isPresent();
    }

    /**
     * Returns an {@code ArrayList} of all UTXOs in the pool
     */
    public List<UTXO> getAllUTXO() {
        return utxoRepository.findAll();
    }
}
