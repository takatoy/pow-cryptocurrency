package wafflecore.test;

import wafflecore.model.KeyPair;
import wafflecore.util.EccService;

/**
 *  Test if key and address are correctly generated.
 */
class TestKeyGenerator {
    public static void main(String[] args) {
        KeyPair keyPair = EccService.generateKey();

        System.out.println(keyPair.publicKey);
        System.out.println(keyPair.privateKey);
        System.out.println(keyPair.address);
    }
}
