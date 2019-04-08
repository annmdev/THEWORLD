package com.example.r.theworld.presentation.model;

import com.google.gson.annotations.SerializedName;

public class WeatherResponse {

    @SerializedName("location")
    public Location location;

    @SerializedName("current")
    public Current current;

    @SerializedName("forecast")
    public Forecast forecast;
}
