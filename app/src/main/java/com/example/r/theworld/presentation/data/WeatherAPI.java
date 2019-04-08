package com.example.r.theworld.presentation.data;

import com.example.r.theworld.presentation.model.WeatherResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherAPI {

    @GET("/v1/forecast.json")
    Call<WeatherResponse> getWeatherForecast(
        @Query("q") String q,
        @Query("days") int days
    );

    @GET("/v1/current.json")
    Call<WeatherResponse> getCurrentWeather(
            @Query("q") String q
    );
}
