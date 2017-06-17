package wafflecore.util;

import wafflecore.model.KeyPair;

public class EccService {
    public static KeyPair generateKey() {
        byte[] publicKey = new byte[32];
        byte[] privateKey = new byte[32];
        byte[] address = new byte[32]; // SHA256(RIPEMD160(publicKey))でハッシュしたもの (最悪二重SHA256ハッシュでも可)

        // publicKeyとprivateKeyの生成を実装

        return new KeyPair(publicKey, privateKey, address);
    }

    public static byte[] sign() {
        // ECDSA署名
        return new byte[32];
    }

    public static boolean verify(byte[] hash, byte[] signature, byte[] publicKey) {
        // 署名の正当性検証
        return false;
    }
}
