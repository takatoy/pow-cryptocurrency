package wafflecore;

import java.util.Arrays;

public class WaffleBase58 {
    private static final char[] B58CHARS = "123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz".toCharArray();
    private static final char B58ZERO = B58CHARS[0];
    private static final int[] B58INDEX = new int[128];
    static {
        Arrays.fill(B58INDEX, -1);
        for (int i = 0; i < B58CHARS.length; i++) {
            B58INDEX[B58CHARS[i]] = i;
        }
    }

    public static String encode(byte[] plain) {
        if (plain.length == 0) {
            return "";
        }

        int zeros = 0;
        while (zeros < plain.length && plain[zeros] == 0) {
            zeros++;
        }

        plain = Arrays.copyOf(plain, plain.length); // Original plain won't be changed
        char[] encoded = new char[plain.length * 2];
        int outputStart = encoded.length;
        for (int inputStart = zeros; inputStart < plain.length; ) {
            encoded[--outputStart] = B58CHARS[divmod(plain, inputStart, 256, 58)];
            if (plain[inputStart] == 0) {
                inputStart++;
            }
        }

        while (outputStart < encoded.length && encoded[outputStart] == B58ZERO) {
            outputStart++;
        }
        while (--zeros >= 0) {
            encoded[--outputStart] = B58ZERO;
        }

        return new String(encoded, outputStart, encoded.length - outputStart);
    }

    public static byte[] decode(String encoded) {
        if (encoded.length == 0) {
            return new byte[0];
        }

        // WIP
    }

    private static byte divmod(byte[] n, int firstDigit, int base, int div) {
        int rem = 0;
        for (int i = firstDigit; i < n.length; i++) {
            int digit = (int) n[i] & 0xff;
            int tmp = rem * base + digit;
            n[i] = (byte) (tmp / div);
            rem = tmp % div;
        }
        return (byte) rem;
    }
}
