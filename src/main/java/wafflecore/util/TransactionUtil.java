package wafflecore.util;

import wafflecore.model.*;
import wafflecore.util.Hasher;
import wafflecore.util.ByteArrayWrapper;

import java.util.ArrayList;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TransactionUtil {
    public static Transaction deserializeTransaction(byte[] data) {
        String dataStr = new String(data);

        // Json to transaction.
        ObjectMapper mapper = new ObjectMapper();
        mapper.addMixIn(Transaction.class, TransactionMixIn.class);

        Transaction tx = null;
        try {
            tx = mapper.readValue(dataStr, Transaction.class);
            tx.setOriginal(data);
            tx.setId(computeTransactionId(data));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return tx;
    }

    public static ByteArrayWrapper computeTransactionId(byte[] data) {
        return ByteArrayWrapper.copyOf(Hasher.doubleSha256(data));
    }

    public static byte[] serializeTransaction(Transaction tx) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.addMixIn(Transaction.class, TransactionMixIn.class);

        byte[] serialized = null;

        try {
            serialized = mapper.writeValueAsBytes(tx);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return serialized;
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
