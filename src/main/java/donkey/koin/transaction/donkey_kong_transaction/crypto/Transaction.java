package donkey.koin.transaction.donkey_kong_transaction.crypto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;

@Getter
@Setter
@Document
public class Transaction {

    @Id
    @JsonIgnore
    private String hashId;
    private byte[] hash;
    private ArrayList<Input> inputs;
    private ArrayList<Output> outputs;
    private Instant timestamp;
    private byte[] previousHash;

    @Getter
    @Setter
    public static class Input {

        @JsonIgnore
        private String id;
        public byte[] prevTxHash;
        public int outputIndex;
        public byte[] signature;

        public Input(byte[] prevTxHash, int outputIndex) {
            if (prevTxHash == null) {
                this.prevTxHash = null;
            } else {
                this.prevTxHash = Arrays.copyOf(prevTxHash, prevTxHash.length);
            }
            this.outputIndex = outputIndex;
        }
    }

    @Getter
    @Setter
    public static class Output {

        @JsonIgnore
        private String id;
        public double value;
        public byte[] address;

        public Output(double value, byte[] address) {
            this.value = value;
            this.address = address;
        }
    }

    public Transaction() {
        inputs = new ArrayList<>();
        outputs = new ArrayList<>();
        timestamp = Instant.now();
    }

    public void addInput(Input input) {
        inputs.add(input);
    }

    public void addOutput(double value, PublicKey address) {
        Output op = new Output(value, address.getEncoded());
        outputs.add(op);
    }

    @JsonIgnore
    public byte[] getRawTx() {
        ArrayList<Byte> rawTx = new ArrayList<>();
        for (Input in : inputs) {
            byte[] prevTxHash = in.prevTxHash;
            ByteBuffer b = ByteBuffer.allocate(Integer.SIZE / 8);
            b.putInt(in.outputIndex);
            byte[] outputIndex = b.array();
            byte[] signature = in.signature;
            if (prevTxHash != null)
                for (int i = 0; i < prevTxHash.length; i++)
                    rawTx.add(prevTxHash[i]);
            for (int i = 0; i < outputIndex.length; i++)
                rawTx.add(outputIndex[i]);
            if (signature != null)
                for (int i = 0; i < signature.length; i++)
                    rawTx.add(signature[i]);
        }
        for (Output op : outputs) {
            ByteBuffer b = ByteBuffer.allocate(Double.SIZE / 8);
            b.putDouble(op.value);
            byte[] value = b.array();
            byte[] addressBytes = op.address;
            for (int i = 0; i < value.length; i++) {
                rawTx.add(value[i]);
            }
            for (int i = 0; i < addressBytes.length; i++) {
                rawTx.add(addressBytes[i]);
            }

        }
        byte[] tx = new byte[rawTx.size()];
        int i = 0;
        for (Byte b : rawTx)
            tx[i++] = b;
        return tx;
    }

    public void calculateHash() {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(getRawTx());
            hash = md.digest();
        } catch (NoSuchAlgorithmException x) {
            x.printStackTrace(System.err);
        }
    }
}
