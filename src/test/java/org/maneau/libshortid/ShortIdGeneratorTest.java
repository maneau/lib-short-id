package org.maneau.libshortid;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ShortIdGeneratorTest {

    @Test
    public void newShortIdGenerator_fromInvalid_shouldThrowInvalidShortIdException() {

        assertThrows(InvalidShortIdException.class, () -> ShortIdGenerator.build(null));

        assertThrows(InvalidShortIdException.class, () -> new ShortIdGenerator(null));

        assertThrows(InvalidShortIdException.class, () -> ShortIdGenerator.build(""));

        assertThrows(InvalidShortIdException.class, () -> ShortIdGenerator.build("23456"));

    }

    @Test
    public void fromUUID_shouldBeDeterminist() {
        //Given
        UUID uuid = UUID.fromString("d0290cce-e501-44dd-b8a4-64afeb07681d");

        //When
        String shortId = ShortIdGenerator.build().fromUUID(uuid);

        //Then
        assertNotNull(shortId);
        assertEquals("9ensERhWZcJYT8qKJKKxUG", shortId);
    }

    @Test
    public void fromRandomUUID_shouldAlwaysDifferent() {
        //Given
        //When
        String randShortId1 = ShortIdGenerator.build().fromRandomUUID();
        String randShortId2 = ShortIdGenerator.build().fromRandomUUID();

        //Then
        assertNotNull(randShortId1);
        assertNotNull(randShortId2);
        System.out.println(randShortId2);
        assertTrue(21 <= randShortId1.length());
        assertTrue(21 <= randShortId2.length());
        assertNotEquals(randShortId1, randShortId2);
    }

    @Test
    public void fromLong_then_convertShortIdToLong_shouldBeRetrieve() {
        //Given
        Long expectedLongId = UUID.randomUUID().getMostSignificantBits();
        if(expectedLongId <0) {
            expectedLongId = Long.MAX_VALUE + expectedLongId;
        }
        ShortIdGenerator shortIdGenerator = ShortIdGenerator.build();

        //When
        String shortId = shortIdGenerator.fromLong(expectedLongId);
        Long retrivedLongId = shortIdGenerator.convertShortIdToLong(shortId);

        //Then
        assertNotNull(shortId);
        assertNotNull(retrivedLongId);
        assertNotEquals(0, shortId.length());
        assertEquals(expectedLongId, retrivedLongId);
    }

    @Test
    public void convertShortIdToUUID_shouldRetrieveOriginalUUID() {
        //Given
        UUID expectedUuid = UUID.randomUUID();

        //When
        String shortId = ShortIdGenerator.build().fromUUID(expectedUuid);
        UUID retrievedUuid = ShortIdGenerator.build().convertShortIdToUUID(shortId);

        //Then
        assertNotNull(shortId);
        assertNotNull(retrievedUuid);
        assertEquals(expectedUuid, retrievedUuid);
    }

    @Test
    public void fromUUID_ofInvalid_ShouldThrowAnInvalidShortIdException() {

        assertThrows(InvalidShortIdException.class, () -> ShortIdGenerator.build().fromUUID(null));

        assertThrows(InvalidShortIdException.class, () -> ShortIdGenerator.build().fromLong(null));

    }

    @Test
    public void convertShortIdToUUID_ofInvalid_ShouldThrowAnInvalidShortIdException() {

        assertThrows(InvalidShortIdException.class, () -> ShortIdGenerator.build().convertShortIdToUUID(null));

        assertThrows(InvalidShortIdException.class, () -> ShortIdGenerator.build().convertShortIdToUUID(""));

        assertThrows(InvalidShortIdException.class, () -> ShortIdGenerator.build().convertShortIdToUUID("INVALID_CHAR_@"));

    }

    @Test
    public void fromUUID_then_convertShortIdToUUID_withSpecificCharList_shouldRetrieveOriginalUUID() {
        //Given
        UUID expectedUuid = UUID.randomUUID();
        ShortIdGenerator shortIdGenerator = ShortIdGenerator.build("0123456789_@^-#%§");
        String defaultShortId = ShortIdGenerator.build().fromUUID(expectedUuid);

        //When
        String shortId = shortIdGenerator.fromUUID(expectedUuid);
        UUID retrievedUuid = shortIdGenerator.convertShortIdToUUID(shortId);

        //Then
        assertNotNull(shortId);
        assertTrue(shortId.contains("@") || shortId.contains("_")|| shortId.contains("-"));
        assertNotNull(retrievedUuid);
        assertEquals(expectedUuid, retrievedUuid);
        assertNotEquals(shortId, defaultShortId);
    }

    @Test
    public void fromRandomLong_shouldAlwaysDifferent() {
        //Given
        //When
        String randShortId1 = ShortIdGenerator.build().fromRandomLong();
        String randShortId2 = ShortIdGenerator.build().fromRandomLong();

        //Then
        assertNotNull(randShortId1);
        assertNotNull(randShortId2);
        assertTrue(5 <= randShortId1.length());
        assertTrue(5 <= randShortId2.length());
        assertNotEquals(randShortId1, randShortId2);
    }

    @Test
    public void fromLong_then_convertShortIdToLong_withSpecificCharList_shouldRetrieveOriginalLong() {
        //Given
        long expectedLong = UUID.randomUUID().getMostSignificantBits();
        ShortIdGenerator shortIdGenerator = ShortIdGenerator.build("0123456789_@^-#%§");
        String defaultShortId = ShortIdGenerator.build().fromLong(expectedLong);

        //When
        String shortId = shortIdGenerator.fromLong(expectedLong);
        Long retrievedLong = shortIdGenerator.convertShortIdToLong(shortId);

        //Then
        assertNotNull(shortId);
        assertTrue(shortId.contains("@") || shortId.contains("_")|| shortId.contains("-"));
        assertNotNull(retrievedLong);
        assertEquals(expectedLong, retrievedLong);
        assertNotEquals(shortId, defaultShortId);
    }

}