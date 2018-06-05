package donkey.koin.transaction.donkey_kong_transaction.utils;

import com.google.common.hash.Hashing;

import java.nio.charset.StandardCharsets;

public class StringUtil {
    public static String applySha256(String input) {
        return Hashing.sha256()
                .hashString(input, StandardCharsets.UTF_8)
                .toString();
    }
}