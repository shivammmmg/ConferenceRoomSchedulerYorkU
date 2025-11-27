package shared.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * RoomStatusTest — Unit tests for the {@link RoomStatus} enum
 * and integration tests verifying Room status mapping behavior.
 * ----------------------------------------------------------------------
 * <p>This suite ensures the RoomStatus enumeration behaves as expected,
 * including ordinal ordering, presence of all enum constants, correct
 * {@code valueOf()} resolution, and consistency with how a Room maps
 * string-based status values into enum values.</p>
 *
 * <h2>Test Coverage Goals</h2>
 * <ul>
 *     <li>Verify the total enum constant count</li>
 *     <li>Confirm expected ordinal ordering</li>
 *     <li>Ensure presence of key statuses (MAINTENANCE, UNKNOWN, etc.)</li>
 *     <li>Validate {@code valueOf()} lookups</li>
 *     <li>Check uniqueness between similar statuses</li>
 *     <li>Test {@link Room#setStatus(String)} correctly maps CSV strings into enums</li>
 *     <li>Achieve 100% enum coverage + mapping logic coverage</li>
 * </ul>
 */
class RoomStatusTest {

    /**
     * Test 1 — Validates that RoomStatus defines exactly nine enum constants.
     */
    @Test
    void testNumberOfStatusesIsNine() {
        RoomStatus[] statuses = RoomStatus.values();
        assertEquals(9, statuses.length);
    }

    /**
     * Test 2 — Ensures that AVAILABLE is the first constant (ordinal = 0).
     */
    @Test
    void testAvailableOrdinalIsZero() {
        assertEquals(0, RoomStatus.AVAILABLE.ordinal());
    }

    /**
     * Test 3 — Confirms that RESERVED appears second in the enum declaration.
     */
    @Test
    void testReservedOrdinalIsOne() {
        assertEquals(1, RoomStatus.RESERVED.ordinal());
    }

    /**
     * Test 4 — Verifies that the MAINTENANCE constant exists in the enum.
     */
    @Test
    void testEnumContainsMaintenance() {
        boolean found = false;
        for (RoomStatus rs : RoomStatus.values()) {
            if (rs == RoomStatus.MAINTENANCE) {
                found = true;
                break;
            }
        }
        assertTrue(found);
    }

    /**
     * Test 5 — Confirms the presence of the UNKNOWN fallback enum constant.
     */
    @Test
    void testEnumContainsUnknown() {
        boolean found = false;
        for (RoomStatus rs : RoomStatus.values()) {
            if (rs == RoomStatus.UNKNOWN) {
                found = true;
                break;
            }
        }
        assertTrue(found);
    }

    /**
     * Test 6 — Ensures {@code valueOf("IN_USE")} resolves correctly.
     */
    @Test
    void testValueOfReturnsInUse() {
        RoomStatus status = RoomStatus.valueOf("IN_USE");
        assertEquals(RoomStatus.IN_USE, status);
    }

    /**
     * Test 7 — Ensures the NO_SHOW enum name is exactly represented as "NO_SHOW".
     */
    @Test
    void testNoShowNameIsCorrect() {
        assertEquals("NO_SHOW", RoomStatus.NO_SHOW.name());
    }

    /**
     * Test 8 — Confirms ENABLED and DISABLED are distinct enum values.
     */
    @Test
    void testEnabledAndDisabledAreDistinct() {
        assertNotEquals(RoomStatus.ENABLED, RoomStatus.DISABLED);
    }

    /**
     * Test 9 — Confirms that IN_USE and OCCUPIED are not the same
     * (some systems treat them as synonyms but they remain distinct enums).
     */
    @Test
    void testOccupiedAndInUseAreDistinct() {
        assertNotEquals(RoomStatus.OCCUPIED, RoomStatus.IN_USE);
    }

    /**
     * Test 10 — Ensures {@link Room#setStatus(String)} correctly maps
     * "AVAILABLE" to {@link RoomStatus#AVAILABLE}.
     */
    @Test
    void testSetStatusStringAvailableMapsToEnumAvailable() {
        Room room = new Room("R1", "Test Room");
        room.setStatus("AVAILABLE");
        assertEquals(RoomStatus.AVAILABLE, room.getStatusEnum());
    }

    /**
     * Test 11 — Ensures {@link Room#setStatus(String)} maps
     * "DISABLED" to {@link RoomStatus#DISABLED}.
     */
    @Test
    void testSetStatusStringDisabledMapsToEnumDisabled() {
        Room room = new Room("R1", "Test Room");
        room.setStatus("DISABLED");
        assertEquals(RoomStatus.DISABLED, room.getStatusEnum());
    }

    /**
     * Test 12 — Validates that the short CSV alias "MAINT"
     * correctly maps to {@link RoomStatus#MAINTENANCE}.
     */
    @Test
    void testSetStatusStringMaintMapsToEnumMaintenance() {
        Room room = new Room("R1", "Test Room");
        room.setStatus("MAINT"); // short alias used in CSV files
        assertEquals(RoomStatus.MAINTENANCE, room.getStatusEnum());
    }
}
