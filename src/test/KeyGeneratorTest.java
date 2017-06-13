import wafflecore.model.KeyAddress;
import wafflecore.KeyGenerator;

/**
 *  Test if key and address are correctly generated.
 */
class KeyGeneratorTest {
    public static void main(String[] args) {
        KeyAddress keyAddr = KeyGenerator.generateKey();

        System.out.println(keyAddr.publicKey);
        System.out.println(keyAddr.privateKey);
        System.out.println(keyAddr.address);
    }
}
