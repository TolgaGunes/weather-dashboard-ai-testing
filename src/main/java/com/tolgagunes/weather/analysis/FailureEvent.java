package com.tolgagunes.weather.analysis;

public class FailureEvent {
    public final String test;
    public final String city;
    public final int status;
    public final String endpoint;

    public FailureEvent(String test, String city, int status, String endpoint) {
        this.test = test;
        this.city = city;
        this.status = status;
        this.endpoint = endpoint;
    }
}
