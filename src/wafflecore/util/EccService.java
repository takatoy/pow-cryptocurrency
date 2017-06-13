package wafflecore.util;

import wafflecore.model.KeyPair;

class EccService {
    public static KeyPair generateKey() {
        byte[] publicKey;
        byte[] privateKey;
        byte[] address; // SHA256(RIPEMD160(publicKey))でハッシュしたもの (最悪二重SHA256ハッシュでも可)

        // publicKeyとprivateKeyの生成を実装

        return new KeyPair(publicKey, privateKey, address);
    }

    public static byte[] sign() {
        // ECDSA署名
    }

    public static boolean verify() {
        // 署名の正当性検証
    }
}
