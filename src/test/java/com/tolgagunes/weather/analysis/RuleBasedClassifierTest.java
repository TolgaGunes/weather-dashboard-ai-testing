package com.tolgagunes.weather.analysis;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RuleBasedClassifierTest {

    @Test
    void status429_returnsRateLimit() {

        FailureEvent event =
                new FailureEvent("test", "Berlin", 429, "/weather");

        FailureCategory result =
                RuleBasedClassifier.classify(event);

        assertEquals(FailureCategory.RATE_LIMIT, result);
    }

    @Test
    void status504_returnsNetworkInstability() {

        FailureEvent event =
                new FailureEvent("test", "Berlin", 504, "/weather");

        FailureCategory result =
                RuleBasedClassifier.classify(event);

        assertEquals(FailureCategory.NETWORK_INSTABILITY, result);
    }

    @Test
    void status404_returnsInvalidTestData() {

        FailureEvent event =
                new FailureEvent("test", "InvalidCity", 404, "/weather");

        FailureCategory result =
                RuleBasedClassifier.classify(event);

        assertEquals(FailureCategory.INVALID_TEST_DATA, result);
    }

    @Test
    void status500_returnsApplicationBug() {

        FailureEvent event =
                new FailureEvent("test", "Berlin", 500, "/weather");

        FailureCategory result =
                RuleBasedClassifier.classify(event);

        assertEquals(FailureCategory.APPLICATION_BUG, result);
    }

    @Test
    void status200_returnsUnknown() {

        FailureEvent event =
                new FailureEvent("test", "Berlin", 200, "/weather");

        FailureCategory result =
                RuleBasedClassifier.classify(event);

        assertEquals(FailureCategory.UNKNOWN, result);
    }
}