package org.maneau.libshortid;

import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class UUIDConverterTest {

    @Test
    public void uuid_shouldBeSameAfterExtracting2longs() {
        //Given
        UUID expectedUUID = UUID.randomUUID();

        long mostSignificantBits = expectedUUID.getMostSignificantBits();
        long leastSignificantBits = expectedUUID.getLeastSignificantBits();

        //When
        UUID convertedUUID = new UUID(mostSignificantBits, leastSignificantBits);

        //Then
        assertNotNull(convertedUUID);
        assertEquals(expectedUUID, convertedUUID);
    }

    @Test
    public void toBigInt_shouldBeTheSameAfterConverting() {
        //Given
        UUID expectedUUID = UUID.randomUUID();

        //When
        BigInteger convertedBigInteger = UUIDConverter.toBigInt(expectedUUID);
        UUID convertedUUID = UUIDConverter.fromBigInt(convertedBigInteger);

        //Then
        assertNotNull(convertedBigInteger);
        assertEquals(expectedUUID, convertedUUID);
    }

    @Test
    public void fromBigInt_ofInvalid_shouldThrowInvalidShortIdException() {
        assertThrows(InvalidShortIdException.class, () -> UUIDConverter.fromBigInt(null));
    }

    @Test
    public void toBigInt_ofInvalid_shouldThrowInvalidShortIdException() {
        assertThrows(InvalidShortIdException.class, () -> UUIDConverter.toBigInt(null));
    }
}