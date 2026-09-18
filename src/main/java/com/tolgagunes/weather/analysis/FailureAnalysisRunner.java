package com.tolgagunes.weather.analysis;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.file.Files;

public class FailureAnalysisRunner {

    public static void main(String[] args) throws Exception {

        Path log = Path.of("logs/flaky-run-1.log");

        List<FailureEvent> events =
                FailureLogParser.allFlakyEvents(log);

        if (events.isEmpty()) {
            System.out.println("No [FLAKY_TEST_LOG] events found.");
            return;
        }

        List<FailureAnalysisReport.FailureDetail> failureDetails =
                new ArrayList<>();

        int totalEvents = events.size();
        int failureEvents = 0;

        for (FailureEvent event : events) {

            if (event.status == 200) {
                continue;
            }

            failureEvents++;

            FailureCategory category =
                    RuleBasedClassifier.classify(event);

            failureDetails.add(
                    new FailureAnalysisReport.FailureDetail(
                            event.test,
                            event.city,
                            event.status,
                            event.endpoint,
                            category
                    )
            );
        }

        int passedEvents = totalEvents - failureEvents;

        double passRate =
                ((double) passedEvents / totalEvents) * 100;

        FailureAnalysisReport report =
                new FailureAnalysisReport(
                        totalEvents,
                        passedEvents,
                        failureEvents,
                        passRate,
                        failureDetails
                );

        System.out.println(
                "REPORT totalEvents=" + report.totalEvents +
                        " passedEvents=" + report.passedEvents +
                        " failureEvents=" + report.failureEvents +
                        " passRate=" + report.passRate
        );

        Path reportPath =
                Path.of("target/reports/failure-analysis.json");

        Files.createDirectories(reportPath.getParent());

        ObjectMapper objectMapper = new ObjectMapper();

        objectMapper
                .writerWithDefaultPrettyPrinter()
                .writeValue(reportPath.toFile(), report);

        System.out.println(
                "JSON report created: " + reportPath
        );

    }
}