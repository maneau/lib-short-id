package org.maneau.libshortid;

import java.math.BigInteger;
import java.util.UUID;

public class UUIDConverter {

    public static final BigInteger TWO_SQUARE_64 = BigInteger.ONE.shiftLeft(64); // 2^64
    public static final BigInteger LONG_MAX_VALUE = BigInteger.valueOf(Long.MAX_VALUE);

    public static BigInteger toBigInt(UUID uuid) {
        if(uuid == null) {
            throw new InvalidShortIdException("bigInteger should not be null");
        }
        BigInteger lo = BigInteger.valueOf(uuid.getLeastSignificantBits());
        BigInteger hi = BigInteger.valueOf(uuid.getMostSignificantBits());

        if (hi.signum() < 0) {
            hi = hi.add(TWO_SQUARE_64);
        }

        if (lo.signum() < 0) {
            lo = lo.add(TWO_SQUARE_64);
        }

        return lo.add(hi.multiply(TWO_SQUARE_64));
    }

    public static UUID fromBigInt(BigInteger bigInteger) {
        if(bigInteger == null) {
            throw new InvalidShortIdException("bigInteger should not be null");
        }
        BigInteger[] parts = bigInteger.divideAndRemainder(TWO_SQUARE_64);
        BigInteger hi = parts[0];
        BigInteger lo = parts[1];

        if (LONG_MAX_VALUE.compareTo(hi) < 0) {
            hi = hi.subtract(TWO_SQUARE_64);
        }

        if (LONG_MAX_VALUE.compareTo(lo) < 0) {
            lo = lo.subtract(TWO_SQUARE_64);
        }

        return new UUID(hi.longValueExact(), lo.longValueExact());
    }
}
