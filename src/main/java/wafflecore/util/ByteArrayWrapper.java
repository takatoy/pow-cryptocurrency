package wafflecore.util;

import java.util.Arrays;
import java.util.Base64;
import com.fasterxml.jackson.annotation.JsonValue;

public final class ByteArrayWrapper {
    private final byte[] data;

    public ByteArrayWrapper(byte[] data) {
        if (data == null) {
            throw new NullPointerException();
        }
        this.data = data;
    }

    public static ByteArrayWrapper copyOf(byte[] bytes) {
        return new ByteArrayWrapper((byte[])bytes.clone());
    }

    @JsonValue
    public byte[] getData() {
        return data;
    }

    @Override
    public String toString() {
        return new String(Base64.getEncoder().encode(data));
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof ByteArrayWrapper)) {
            return false;
        }
        return Arrays.equals(data, ((ByteArrayWrapper)other).data);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(data);
    }
}
