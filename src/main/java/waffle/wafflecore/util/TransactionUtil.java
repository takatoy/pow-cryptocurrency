package waffle.wafflecore.util;

import waffle.wafflecore.model.*;
import waffle.wafflecore.util.Hasher;
import waffle.wafflecore.util.ByteArrayWrapper;

import java.util.ArrayList;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TransactionUtil {
    public static ObjectMapper mapper = new ObjectMapper();
    static {
        mapper.addMixIn(Transaction.class, TransactionMixIn.class);
    }

    public static Transaction deserialize(byte[] data) {
        String dataStr = new String(data);

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

    public static byte[] serialize(Transaction tx) {
        byte[] serialized = null;

        try {
            serialized = mapper.writeValueAsBytes(tx);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return serialized;
    }

    public static byte[] getTransactionSignHash(byte[] data) {
        Transaction tx = deserialize(data);

        ArrayList<InEntry> inEntries = tx.getInEntries();
        for (InEntry in : inEntries) {
            in.setPublicKey(null);
            in.setSignature(null);
        }
        tx.setInEntries(inEntries);

        byte[] bytes = serialize(tx);
        return Hasher.doubleSha256(bytes);
    }
}
