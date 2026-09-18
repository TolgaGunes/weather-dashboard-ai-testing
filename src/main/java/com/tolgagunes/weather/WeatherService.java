package com.tolgagunes.weather;

import com.tolgagunes.weather.dto.WeatherResponse;
import com.tolgagunes.weather.exception.CityNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

@Service
public class WeatherService {

    private final RestClient restClient;

    @Value("${openweather.api-key}")
    private String apiKey;

    @Value("${openweather.units}")
    private String units;

    public WeatherService(
            @Value("${openweather.base-url}") String baseUrl
    ) {
        this.restClient = RestClient.builder()
                .baseUrl(baseUrl)
                .build();
    }

    public WeatherResponse getWeather(String city) {

        try {
            return restClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/weather")
                            .queryParam("q", city)
                            .queryParam("appid", apiKey)
                            .queryParam("units", units)
                            .build())
                    .retrieve()
                    .body(WeatherResponse.class);

        } catch (HttpClientErrorException.NotFound e) {
            throw new CityNotFoundException(city);
        }
    }
}