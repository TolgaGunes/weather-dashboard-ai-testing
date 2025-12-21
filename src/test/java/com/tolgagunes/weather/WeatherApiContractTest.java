
package com.tolgagunes.weather;

import com.tolgagunes.weather.dto.WeatherResponse;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class WeatherApiContractTest {

    @LocalServerPort
    int port;

    @Autowired
    TestRestTemplate rest;

    @Test
    void weatherBerlin_returns200_andHasExpectedFields() {
        String url = "http://localhost:" + port + "/weather?city=Berlin";

        ResponseEntity<WeatherResponse> response =
                rest.getForEntity(url, WeatherResponse.class);

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());

        WeatherResponse body = response.getBody();
        assertEquals("Berlin", body.name);
        assertNotNull(body.main);
        assertTrue(body.main.temp > -100 && body.main.temp < 100); // mantıklı aralık
        assertNotNull(body.weather);
        assertTrue(body.weather.length > 0);
        assertNotNull(body.weather[0].description);
        assertFalse(body.weather[0].description.isBlank());
    }

    @Test
    void weatherInvalidCity_returnsErrorStatus() {
        String url = "http://localhost:" + port + "/weather?city=ThisCityShouldNotExist_12345";

        ResponseEntity<String> response =
                rest.getForEntity(url, String.class);

        int status = response.getStatusCode().value();

        boolean valid = status == 400 || status == 404 || status == 500;
        assertTrue(valid, "Unexpected status code: " + status + " body=" + response.getBody());
    }


    @Tag("flaky-sim")
    @Test
    void flakyWeatherTest_collectsFailures_forAnalysis() {

        System.out.println(">>> FLAKY TEST EXECUTED <<<");

        String testName = "flakyWeatherTest";
        String endpoint = "/weather";

        int failures = 0;

        for (int i = 1; i <= 5; i++) {

            String city = Math.random() > 0.5
                    ? "Frankfurt"
                    : "InvalidCity_" + System.nanoTime();

            String url = "http://localhost:" + port + "/weather?city=" + city;

            ResponseEntity<String> response = rest.getForEntity(url, String.class);
            int status = response.getStatusCode().value();

            System.out.println(
                    "[FLAKY_TEST_LOG] " +
                            "test=" + testName + " " +
                            "attempt=" + i + " " +
                            "city=" + city + " " +
                            "status=" + status + " " +
                            "endpoint=" + endpoint
            );

            if (status != 200) {
                failures++;
            }
        }

        // Bu testin amacı flaky failure üretip analiz etmek.
        // En sonda fail sayısını raporlayıp (istersen) kontrollü fail edebilirsin.
        assertTrue(failures >= 0, "failures=" + failures);
    }





}
