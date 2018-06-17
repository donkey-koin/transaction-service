package donkey.koin.transaction.donkey_kong_transaction.crypto;

import donkey.koin.transaction.donkey_kong_transaction.entities.UTXO;
import donkey.koin.transaction.donkey_kong_transaction.repo.UTXORepository;
import org.apache.commons.lang.ArrayUtils;

import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

public class TxHandler {

    private UTXORepository utxoRepository;
    private double totalInputSum;

    /**
     * Creates a public ledger whose current UTXOService (collection of unspent transaction outputs) is
     * {@code utxoService}. This should make a copy of utxoService by using the UTXOService(UTXOService uPool)
     * constructor.
     */
    public TxHandler(UTXORepository utxoRepository) {
        this.utxoRepository = utxoRepository;
        this.totalInputSum = 0;
    }

    /**
     * @return true if:
     * (1) all outputs claimed by {@code tx} are in the current UTXO utxoRepository,
     * (2) the signatures on each input of {@code tx} are valid,
     * (3) no UTXO is claimed multiple times by {@code tx},
     * (4) all of {@code tx}s output values are non-negative, and
     * (5) the sum of {@code tx}s input values is greater than or equal to the sum of its output
     * values; and false otherwise.
     */
    public boolean isValidTx(Transaction tx) {
        this.totalInputSum = 0;
        return validateRuleNumber12And3(tx) &&
                validateRuleNumber4And5(tx);
    }

    private boolean validateRuleNumber12And3(Transaction tx) {
        HashMap<UTXO, Boolean> usedUTXO = new HashMap<>();

        for (int i = 0; i < tx.numInputs(); i++) {
            Transaction.Input input = tx.getInput(i);
            if (input == null) {
                return false;
            }

            UTXO utxo = new UTXO(input.prevTxHash, input.outputIndex);
            //rule number 1
            Optional<UTXO> oUtxo = utxoRepository.findByTxHash(input.prevTxHash);
            if (!oUtxo.isPresent()) {
                return false;
            }

            PublicKey publicKey = getPublicKeyFromUTxo(oUtxo);

            if (publicKey == null) {
                return false;
            }

            byte[] message = tx.getRawDataToSign(i);
            byte[] signature = input.signature;
            //rule number 2
            if (!Crypto.verifySignature(publicKey, message, signature)) {
                return false;
            }

            //rule number 3
            if (usedUTXO.containsKey(utxo)) {
                return false;
            }

            usedUTXO.put(utxo, true);

            //saving this value for rule number 5
            this.totalInputSum += oUtxo.get().getValue();
        }

        return true;
    }

    private PublicKey getPublicKeyFromUTxo(Optional<UTXO> oUtxo) {
        byte[] bytes = oUtxo.get().getAddress();
        PublicKey publicKey = null;
        try {
            publicKey = getRsaPublicKeyKeyFromBytes(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return publicKey;
    }

    private boolean validateRuleNumber4And5(Transaction tx) {
        double outputSum = 0;

        for (int i = 0; i < tx.numOutputs(); i++) {
            Transaction.Output output = tx.getOutput(i);
            if (output == null) {
                return false;
            }
            if (output.value < 0) {
                return false;
            }

            outputSum += output.value;
        }

        return this.totalInputSum >= outputSum;
    }

    /**
     * Handles each epoch by receiving an unordered array of proposed transactions, checking each
     * transaction for correctness, returning a mutually valid array of accepted transactions, and
     * updating the current UTXO utxoRepository as appropriate.
     */
    public Transaction[] handleTxs(Transaction[] possibleTxs) {
        if (possibleTxs == null) {
            return new Transaction[0];
        }

        ArrayList<Transaction> validTxs = new ArrayList<>();

        for (Transaction tx : possibleTxs) {
            if (!isValidTx(tx)) {
                continue;
            }
            validTxs.add(tx);

            for (Transaction.Input input : tx.getInputs()) {
                this.utxoRepository.deleteByTxHash(input.prevTxHash);
            }
            byte[] txHash = tx.getHash();
            int index = 0;
            for (Transaction.Output output : tx.getOutputs()) {
                UTXO utxo = new UTXO(txHash, index);
                utxo.setAddress(output.address);
                utxo.setValue(output.value);
                index += 1;
                this.utxoRepository.save(utxo);
            }
        }

        return validTxs.toArray(new Transaction[validTxs.size()]);
    }

    public static PublicKey getPublicKeyFromBoxedBytes(Byte[] bytes){
        return getRsaPublicKeyKeyFromBytes(ArrayUtils.toPrimitive(bytes));
    }

    public static PublicKey getRsaPublicKeyKeyFromBytes(byte[] bytes) {
        try {
            return KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(bytes));
        } catch (Exception e) {
            return null;
        }
    }
}
