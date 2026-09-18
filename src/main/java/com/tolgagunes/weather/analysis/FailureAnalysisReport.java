package com.tolgagunes.weather.analysis;

import java.util.List;

public class FailureAnalysisReport {

    public final int totalEvents;
    public final int passedEvents;
    public final int failureEvents;
    public final double passRate;
    public final List<FailureDetail> failures;

    public FailureAnalysisReport(
            int totalEvents,
            int passedEvents,
            int failureEvents,
            double passRate,
            List<FailureDetail> failures
    ) {
        this.totalEvents = totalEvents;
        this.passedEvents = passedEvents;
        this.failureEvents = failureEvents;
        this.passRate = passRate;
        this.failures = failures;
    }

    public static class FailureDetail {

        public final String test;
        public final String city;
        public final int status;
        public final String endpoint;
        public final FailureCategory category;

        public FailureDetail(
                String test,
                String city,
                int status,
                String endpoint,
                FailureCategory category
        ) {
            this.test = test;
            this.city = city;
            this.status = status;
            this.endpoint = endpoint;
            this.category = category;
        }
    }
}