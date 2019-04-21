package com.example.r.theworld.presentation.model;

import com.google.gson.annotations.SerializedName;

public class Current {

    @SerializedName("temp_c")
    public double temp_c;

    @SerializedName("is_day")
    public int isDay;

    @SerializedName("condition")
    public Condition condition;

    @SerializedName("wind_kph")
    public double wind;

    @SerializedName("pressure_mb")
    public double pressure;

    @SerializedName("humidity")
    public int humidity;

    @SerializedName("precip_mm")
    public double precipitation;

    @SerializedName("feelslike_c")
    public double feelslikeTemp;
}
