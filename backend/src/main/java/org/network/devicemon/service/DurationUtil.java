package org.network.devicemon.service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Provides utility methods for parsing RouterOS duration values
 */
public final class DurationUtil {

    private static Pattern PATTERN = Pattern.compile("([0-9]*w)?([0-9]*d)?([0-9]*h)?([0-9]*m)?([0-9]*s)?");

    private static int[] MULTIPLIER = {86400 * 7, 86400, 3600, 60, 1};

    private DurationUtil() { }

    /**
     * @param durationAsString Proprietary duration in the format "2h10m5s" or "1w2d12h24m40s". This format is used by mikrotik.
     * @return seconds representation of the duration
     */
    public static int durationToSeconds(final String durationAsString) {
        // handle 'never'
        if (durationAsString.equals("never")) {
            return 0;
        }

        int duration = 0;

        Matcher matcher = PATTERN.matcher(durationAsString);
        if (matcher.find() && matcher.groupCount() > 1) {
            for (int i = 0; i < matcher.groupCount(); i++) {
                String group = matcher.group(i + 1);
                if (group != null) {
                    int multiplier = MULTIPLIER[i];
                    int groupValue = Integer.parseInt(group.substring(0, group.length() - 1));
                    duration += groupValue * multiplier;
                }
            }
        }
        return duration;
    }
}
