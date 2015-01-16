package security;

import java.math.BigInteger;

public class Length {

    enum Status {
        INT, LONG, BIGINT
    }

    private static final int INT_MAX = Integer.MAX_VALUE - 8;

    private static final long LONG_MAX = Long.MAX_VALUE - 8L;

    int iValue;

    long lValue;

    BigInteger biValue;

    Status status = Status.INT;

    private final int size;

    public Length(int size) {
        this.size = size;
    }

    public void increment() {
        switch (status) {
        case INT: {
            if (iValue > INT_MAX) {
                lValue = iValue;
                status = Status.LONG;
                increment();
                return;
            }
            iValue += 8;
            return;
        }
        case LONG: {
            if (lValue > LONG_MAX) {
                biValue = BigInteger.valueOf(lValue);
                status = Status.BIGINT;
                increment();
                return;
            }
            lValue += 8L;
            return;
        }
        case BIGINT: {
            biValue = biValue.add(BigInteger.valueOf(8));
            return;
        }
        }
        throw new IllegalStateException();
    }

    private byte getByte(int index) {
        switch (status) {
        case INT: {
            if (index < 4) {
                return (byte) (iValue >> (8 * index));
            }
            return 0;
        }
        case LONG: {
            if (index < 8) {
                return (byte) (lValue >> (8L * index));
            }
            return 0;
        }
        case BIGINT: {
            return biValue.shiftRight(8 * index).byteValue();
        }
        }
        throw new IllegalStateException();
    }

    public void writeTo(byte[] out, int index) {
        for (int i = 0; i < size; i++) {
            out[index + i] = getByte(size - 1 - i);
        }
    }

}
