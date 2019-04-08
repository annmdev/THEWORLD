package com.example.r.theworld.presentation.model;

import com.google.gson.annotations.SerializedName;

public class DayWeatherForecast {

    @SerializedName("date")
    public String date;

    @SerializedName("day")
    public Day day;

    @SerializedName("astro")
    public Astro astro;
}
