package donkey.koin.transaction.donkey_kong_transaction.entities;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.security.PublicKey;
import java.util.Arrays;

@Data
@Document
public class UTXO implements Comparable<UTXO> {

    @Id
    private String id;
    private byte[] txHash;
    private int index;
    private double value;
    private byte[] address;

    public UTXO(byte[] txHash, int index) {
        this.txHash = Arrays.copyOf(txHash, txHash.length);
        this.index = index;
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }
        if (getClass() != other.getClass()) {
            return false;
        }

        UTXO utxo = (UTXO) other;
        byte[] hash = utxo.txHash;
        int in = utxo.index;
        if (hash.length != txHash.length || index != in) {
            return false;
        }
        for (int i = 0; i < hash.length; i++) {
            if (hash[i] != txHash[i]) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 1;
        hash = hash * 17 + index;
        hash = hash * 31 + Arrays.hashCode(txHash);
        return hash;
    }

    @Override
    public int compareTo(UTXO utxo) {
        byte[] hash = utxo.txHash;
        int in = utxo.index;
        if (in > index) {
            return -1;
        } else if (in < index) {
            return 1;
        } else {
            int len1 = txHash.length;
            int len2 = hash.length;
            if (len2 > len1) {
                return -1;
            } else if (len2 < len1) {
                return 1;
            } else {
                for (int i = 0; i < len1; i++) {
                    if (hash[i] > txHash[i]) {
                        return -1;
                    } else if (hash[i] < txHash[i]) {
                        return 1;
                    }
                }
                return 0;
            }
        }
    }
}
