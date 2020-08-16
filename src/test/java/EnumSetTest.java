import org.testng.Assert;
import org.testng.annotations.Test;
import sun.misc.SharedSecrets;

import java.util.EnumSet;
import java.util.Set;

public class EnumSetTest {

    @Test
    public void testOf() {
        final Set<ProfileType> profileTypes = EnumSet.of(ProfileType.ADMIN);

        Assert.assertEquals(profileTypes.size(), 1);
        Assert.assertTrue(profileTypes.contains(ProfileType.ADMIN));
    }

    @Test
    public void testAllOf() {
        final Set<ProfileType> profileTypes = EnumSet.allOf(ProfileType.class);

        Assert.assertEquals(profileTypes.size(), 4);
    }

    @Test
    public void testNoneOf() {
        final Set<ProfileType> profileTypes = EnumSet.noneOf(ProfileType.class);

        Assert.assertEquals(profileTypes.size(), 0);
    }

    @Test
    public void testComplementOf() {
        final EnumSet<ProfileType> onlyStaff = EnumSet.of(ProfileType.ADMIN, ProfileType.MODERATOR);

        final EnumSet<ProfileType> onlyClients = EnumSet.complementOf(onlyStaff);

        Assert.assertEquals(onlyClients.size(), 2);
        Assert.assertTrue(onlyClients.contains(ProfileType.GUEST));
        Assert.assertTrue(onlyClients.contains(ProfileType.USER));
    }

    @Test
    public void testCopyOf() {
        final EnumSet<ProfileType> onlyAdmin = EnumSet.of(ProfileType.ADMIN);

        final EnumSet<ProfileType> copiedSet = EnumSet.copyOf(onlyAdmin);

        Assert.assertEquals(copiedSet.size(), 1);
        Assert.assertTrue(copiedSet.contains(ProfileType.ADMIN));
    }

    @Test
    public void testRange() {
        //самый опасный метод, который зависит от порядка расположения перечислений в классе
        final EnumSet<ProfileType> rangeSet = EnumSet.range(ProfileType.GUEST, ProfileType.MODERATOR);

        Assert.assertEquals(rangeSet.size(), 3);
        Assert.assertFalse(rangeSet.contains(ProfileType.ADMIN));
    }

    @Test
    public void testRangeFails() {
        Assert.assertThrows(
            IllegalArgumentException.class,
            () -> EnumSet.range(ProfileType.MODERATOR, ProfileType.GUEST)
        );
    }
}
