package com.tolgagunes.weather.analysis;

import java.nio.file.Path;
import java.util.List;

public class FailureAnalysisRunner {

    public static void main(String[] args) throws Exception {
        Path log = Path.of("logs/flaky-run-1.log");

        List<FailureEvent> events = FailureLogParser.allFlakyEvents(log);

        if (events.isEmpty()) {
            System.out.println("No [FLAKY_TEST_LOG] events found.");
            return;
        }

        int total = 0;
        int failures = 0;

        for (FailureEvent e : events) {
            total++;

            // Sadece başarısızları analiz et
            if (e.status == 200) continue;

            failures++;
            FailureCategory category = RuleBasedClassifier.classify(e);

            System.out.println(
                    "FAIL test=" + e.test +
                            " city=" + e.city +
                            " status=" + e.status +
                            " endpoint=" + e.endpoint +
                            " -> category=" + category
            );
        }

        System.out.println("SUMMARY totalEvents=" + total + " failureEvents=" + failures);
    }
}
