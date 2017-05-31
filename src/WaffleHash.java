import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

class WaffleHash {
    public static String sha256(String text) {
        byte[] cipher_byte;

        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(text.getBytes());
            cipher_byte = md.digest();
            StringBuilder sb = new StringBuilder(2 * cipher_byte.length);
            for (byte b: cipher_byte) {
                sb.append(String.format("%02x", b&0xff));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return "";
    }

    /**
     *  Double sha256
     */
    public static String sha256d(String text) {
        return sha256(sha256(text));
    }
}
