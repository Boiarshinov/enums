import org.junit.jupiter.api.Test;

import java.util.EnumSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class EnumSetTest {

    @Test
    public void testOf() {
        final Set<ProfileType> profileTypes = EnumSet.of(ProfileType.ADMIN);

        assertEquals(profileTypes.size(), 1);
        assertTrue(profileTypes.contains(ProfileType.ADMIN));
    }

    @Test
    public void testAllOf() {
        final Set<ProfileType> profileTypes = EnumSet.allOf(ProfileType.class);

        assertEquals(profileTypes.size(), 4);
    }

    @Test
    public void testNoneOf() {
        final Set<ProfileType> profileTypes = EnumSet.noneOf(ProfileType.class);

        assertEquals(profileTypes.size(), 0);
    }

    @Test
    public void testComplementOf() {
        final EnumSet<ProfileType> onlyStaff = EnumSet.of(ProfileType.ADMIN, ProfileType.MODERATOR);

        final EnumSet<ProfileType> onlyClients = EnumSet.complementOf(onlyStaff);

        assertEquals(onlyClients.size(), 2);
        assertTrue(onlyClients.contains(ProfileType.GUEST));
        assertTrue(onlyClients.contains(ProfileType.USER));
    }

    @Test
    public void testCopyOf() {
        final EnumSet<ProfileType> onlyAdmin = EnumSet.of(ProfileType.ADMIN);

        final EnumSet<ProfileType> copiedSet = EnumSet.copyOf(onlyAdmin);

        assertEquals(copiedSet.size(), 1);
        assertTrue(copiedSet.contains(ProfileType.ADMIN));
    }

    @Test
    public void testRange() {
        //самый опасный метод, который зависит от порядка расположения перечислений в классе
        final EnumSet<ProfileType> rangeSet = EnumSet.range(ProfileType.GUEST, ProfileType.MODERATOR);

        assertEquals(rangeSet.size(), 3);
        assertFalse(rangeSet.contains(ProfileType.ADMIN));
    }

    @Test
    public void testRangeFails() {
        assertThrows(
            IllegalArgumentException.class,
            () -> EnumSet.range(ProfileType.MODERATOR, ProfileType.GUEST)
        );
    }
}
