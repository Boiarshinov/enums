package dev.boiarshinov.enumsinjava.collections;

import org.junit.jupiter.api.Test;

import java.util.EnumMap;

public class EnumMapTest {

    private enum OfficerRank {
        LIEUTENANT,
        CAPITAN,
        MAJOR,
        COLONEL,
        GENERAL
    }

    @Test
    void test() {
        final EnumMap<OfficerRank, Integer> rankToStarCount = new EnumMap<>(OfficerRank.class);
        rankToStarCount.put(OfficerRank.LIEUTENANT, 2);
        rankToStarCount.put(OfficerRank.CAPITAN, 4);
        rankToStarCount.put(OfficerRank.MAJOR, 1);
        rankToStarCount.put(OfficerRank.COLONEL, 3);
        rankToStarCount.put(OfficerRank.GENERAL, 1);
    }
}
