package com.tolgagunes.weather.analysis;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class FailureLogParser {

    private static final Charset[] CANDIDATES = new Charset[] {
            StandardCharsets.UTF_8,
            StandardCharsets.UTF_16LE,
            StandardCharsets.UTF_16BE,
            StandardCharsets.UTF_16,
            Charset.forName("windows-1252"),
            Charset.forName("windows-1254"),
            StandardCharsets.ISO_8859_1
    };

    public static FailureEvent firstFlakyEvent(Path logFile) throws Exception {
        byte[] bytes = Files.readAllBytes(logFile);

        String content = decodeBestEffort(bytes);
        String[] lines = content.split("\\R");

        String selected = null;
        for (String l : lines) {
            String n = normalize(l);
            if (n.contains("[FLAKY_TEST_LOG]")) {
                selected = n;
                break;
            }
        }

        if (selected == null) {
            throw new IllegalStateException("No [FLAKY_TEST_LOG] line found in " + logFile.toAbsolutePath());
        }

        return parseLine(selected);
    }

    public static FailureEvent parseLine(String line) {
        String n = normalize(line);

        int idx = n.indexOf("[FLAKY_TEST_LOG]");
        String payload = n.substring(idx + "[FLAKY_TEST_LOG]".length()).trim();

        String[] parts = payload.isEmpty() ? new String[0] : payload.split("\\s+");

        String test = "unknown";
        String city = "unknown";
        String endpoint = "unknown";
        int status = -1;

        for (String p : parts) {
            String[] pair = p.split("=", 2);
            if (pair.length != 2) continue;

            switch (pair[0]) {
                case "test" -> test = pair[1];
                case "city" -> city = pair[1];
                case "status" -> status = safeParseInt(pair[1], -1);
                case "endpoint" -> endpoint = pair[1];
            }
        }

        return new FailureEvent(test, city, status, endpoint);
    }

    private static String decodeBestEffort(byte[] bytes) {
        for (Charset cs : CANDIDATES) {
            String s = new String(bytes, cs);
            if (s.toUpperCase().contains("FLAKY")) {
                return s;
            }
        }
        // Fallback: Latin-1 ile döndür (hiç olmazsa bozmadan taşır)
        return new String(bytes, StandardCharsets.ISO_8859_1);
    }

    private static String normalize(String s) {
        if (s == null) return "";
        return s.replace("\uFEFF", "")
                .replace("\u200B", "")
                .replace("\r", "")
                .trim();
    }

    private static int safeParseInt(String s, int fallback) {
        try {
            return Integer.parseInt(s);
        } catch (Exception e) {
            return fallback;
        }
    }


    public static List<FailureEvent> allFlakyEvents(Path logFile) throws Exception {
        byte[] bytes = Files.readAllBytes(logFile);
        String content = decodeBestEffort(bytes);
        String[] lines = content.split("\\R");

        List<FailureEvent> events = new ArrayList<>();
        for (String l : lines) {
            String n = normalize(l);
            if (n.contains("[FLAKY_TEST_LOG]")) {
                events.add(parseLine(n));
            }
        }
        return events;
    }

}
