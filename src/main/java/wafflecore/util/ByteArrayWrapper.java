package wafflecore.util;

import java.util.Arrays;
import java.util.Base64;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonCreator;

public final class ByteArrayWrapper {
    private final byte[] data;

    public ByteArrayWrapper(byte[] data) {
        if (data == null) {
            throw new NullPointerException();
        }
        this.data = data;
    }

    @JsonCreator
    public static ByteArrayWrapper copyOf(byte[] bytes) {
        return new ByteArrayWrapper((byte[])bytes.clone());
    }

    public int size() {
        return data.length;
    }

    @JsonValue
    public byte[] getBytes() {
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
