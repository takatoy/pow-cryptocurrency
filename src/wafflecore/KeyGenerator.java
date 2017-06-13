class KeyGenerator {
    public static KeyAddress generateKey() {
        byte[] publicKey;
        byte[] privateKey;
        byte[] address; // SHA256(RIPEMD160(publicKey))でハッシュしたもの (最悪二重SHA256ハッシュでも可)

        // publicKeyとprivateKeyの生成

        return new KeyAddress(publicKey, privateKey, address);
    }
}
