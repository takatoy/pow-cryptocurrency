package wafflecore;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Hasher {
    public static byte[] sha256(byte[] plain, int offset, int length) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(plain, offset, length);
            return md.digest();
        } catch (Exception e) {
            // Can't happen
            e.printStackTrace();
        }

        return new byte[0];
    }

    public static byte[] sha256(byte[] plain) {
        return sha256(plain, 0, plain.length);
    }

    /**
     *  @TODO Need to consider how to implement ripemd160
     */
    // public static byte[] ripemd160(byte[] plain) throws Exception {
    //     MessageDigest md = MessageDigest.getInstance("RipeMD160", "BC");
    //     md.update(plain);
    //     byte[] digest = md.digest();
    //     return digest;
    // }

    /**
     *  SHA256(SHA256(plain))
     */
    public static byte[] hash256(byte[] plain) throws Exception {
        return sha256(sha256(plain));
    }

    /**
     *  @TODO RIPEMD160(SHA256(plain))
     */
    // public static byte[] hash160(byte[] plain) throws Exception {
    //     return ripemd160(sha256(plain));
    // }
}
