package com.tolgagunes.weather.analysis;

public class RuleBasedClassifier {

    public static FailureCategory classify(FailureEvent e) {
        if (e.status == 429) return FailureCategory.RATE_LIMIT;
        if (e.status == 408 || e.status == 504) return FailureCategory.NETWORK_INSTABILITY;
        if (e.status >= 400 && e.status < 500) return FailureCategory.INVALID_TEST_DATA;
        if (e.status >= 500) return FailureCategory.APPLICATION_BUG;
        return FailureCategory.UNKNOWN;
    }
}
