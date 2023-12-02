package org.maneau.libshortid;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ShortIdGenerator {

    private final static String DEFAULT_CHAR_LIST = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final long ZERO_LONG = 0L;
    private final BigInteger charListLengthInBigInt;
    private final Long charListLengthInLong;
    private final String charList;
    private final Map<Character, Integer> reverseCharMap;

    private static Map<Character, Integer> generateReverseMap(String charList) {
        if(charList == null || charList.length() < 8) {
            throw new InvalidShortIdException("charList is too small");
        }
        Map<Character, Integer> reverseCharMap = new HashMap<>();
        for(int i = 0; i < charList.length(); i++) {
            reverseCharMap.put(charList.charAt(i), i);
        }
        return reverseCharMap;
    }

    public ShortIdGenerator() {
        this.charList = DEFAULT_CHAR_LIST;
        this.reverseCharMap = generateReverseMap(DEFAULT_CHAR_LIST);
        this.charListLengthInLong = (long) DEFAULT_CHAR_LIST.length();
        this.charListLengthInBigInt = BigInteger.valueOf(charListLengthInLong);
    }

    public ShortIdGenerator(String charList) {
        this.charList = charList;
        this.reverseCharMap = generateReverseMap(charList);
        this.charListLengthInLong = (long) charList.length();
        this.charListLengthInBigInt = BigInteger.valueOf(charListLengthInLong);
    }

    public static ShortIdGenerator build() {
        return new ShortIdGenerator();
    }

    public static ShortIdGenerator build(String charList) {
        return new ShortIdGenerator(charList);
    }

    public String fromRandomUUID() {
        return fromUUID(UUID.randomUUID());
    }

    public String fromUUID(UUID uuid) {
        if(uuid == null) {
            throw new InvalidShortIdException("Invalid uuid");
        }
        final StringBuilder shortId = new StringBuilder();
        BigInteger bigInteger = UUIDConverter.toBigInt(uuid);

        while (bigInteger.compareTo(BigInteger.ZERO) > 0) {
            BigInteger modulo = bigInteger.mod(this.charListLengthInBigInt);
            bigInteger = bigInteger.subtract(modulo).divide(this.charListLengthInBigInt);
            shortId.append(this.charList.charAt(modulo.intValue()));
            //System.out.println("UUID2ShortId " + bigInteger);
        }
        return shortId.toString();
    }

    public String fromLong(Long id) {
        if(id == null || id <=0) {
            throw new InvalidShortIdException("Invalid id");
        }
        final StringBuilder shortId = new StringBuilder();

        while (id > ZERO_LONG) {
            int modulo = (int) (id % this.charListLengthInLong);
            id = (id - modulo) / this.charListLengthInLong;
            shortId.append(this.charList.charAt(modulo));
        }
        return shortId.toString();
    }

    public UUID convertShortIdToUUID(String shortId) {
        if (shortId == null || shortId.isEmpty()) {
            throw new InvalidShortIdException("ShortId should not be empty");
        }
        BigInteger bigInteger = BigInteger.valueOf(0);
        //Read from the right to the left
        for (int i = shortId.length() - 1; i >=0 ; i--) {
            Integer value = this.reverseCharMap.get(shortId.charAt(i));
            if(value == null) {
                throw new InvalidShortIdException("Character not in the map");
            }

            BigInteger originalModulo = BigInteger.valueOf(value);
            bigInteger = bigInteger.multiply(this.charListLengthInBigInt).add(originalModulo);
            //System.out.println("ShortId2UUID " + bigInteger);
        }
        return UUIDConverter.fromBigInt(bigInteger);
    }

    public String fromRandomLong() {
        long mostSignificantBits = UUID.randomUUID().getMostSignificantBits();
        if(mostSignificantBits < 0) {
            mostSignificantBits = Long.MAX_VALUE + mostSignificantBits;
        }
        return fromLong(mostSignificantBits);
    }

    public Long convertShortIdToLong(String shortId) {
        long retrieveId = 0L;
        for (int i = shortId.length() - 1; i >=0 ; i--) {
            Integer modulo = this.reverseCharMap.get(shortId.charAt(i));
            if(modulo == null) {
                throw new InvalidShortIdException("Character not in the map");
            }

            retrieveId = (retrieveId * this.charListLengthInLong) + modulo;
            //System.out.println("ShortId2UUID " + bigInteger);
        }
        return retrieveId;
    }
}
