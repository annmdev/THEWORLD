package com.example.r.theworld.presentation.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Forecast {

    @SerializedName("forecastday")
    public List<DayWeatherForecast> forecastList;
}
