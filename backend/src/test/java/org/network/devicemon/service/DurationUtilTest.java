package org.network.devicemon.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DurationUtilTest {

    @Test
    public void testDurationToSeconds() {
        assertEquals(0, DurationUtil.durationToSeconds("kjahfgdsljkf"));
        assertEquals(0, DurationUtil.durationToSeconds(""));
        assertEquals(0, DurationUtil.durationToSeconds(" "));

        assertEquals(0, DurationUtil.durationToSeconds("never"));
        assertEquals(86400 * 7 + 1, DurationUtil.durationToSeconds("1w1s"));
        assertEquals(86400 * 7 * 10 + 10, DurationUtil.durationToSeconds("10w10s"));
        assertEquals(1, DurationUtil.durationToSeconds("1s"));
        assertEquals(86400 * 7 * 2 + 86400 * 3 + 3600 * 4 + 60 * 5 + 6, DurationUtil.durationToSeconds("2w3d4h5m6s"));
        assertEquals(86400 * 7 * 23 + 86400 * 3 + 3600 * 12 + 60 * 14 + 50, DurationUtil.durationToSeconds("23w3d12h14m50s"));
    }
}
