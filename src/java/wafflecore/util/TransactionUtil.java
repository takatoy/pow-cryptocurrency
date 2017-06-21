package wafflecore.util;

import wafflecore.model.*;
import wafflecore.util.Hasher;
import java.util.ArrayList;

public class TransactionUtil {
    public static Transaction deserializeTransaction(byte[] data) {
        return null;
    }

    public static byte[] serializeTransaction(Transaction tx) {
        return new byte[32];
    }

    public static byte[] getTransactionSignHash(byte[] data) {
        Transaction tx = deserializeTransaction(data);

        ArrayList<InEntry> inEntries = tx.getInEntries();
        for (InEntry in : inEntries) {
            in.setPublicKey(null);
            in.setSignature(null);
        }
        tx.setInEntries(inEntries);

        byte[] bytes = serializeTransaction(tx);
        return Hasher.doubleSha256(bytes);
    }
}
