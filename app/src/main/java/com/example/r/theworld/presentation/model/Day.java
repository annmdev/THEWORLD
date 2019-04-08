package com.example.r.theworld.presentation.model;

import com.google.gson.annotations.SerializedName;

public class Day {

    @SerializedName("avgtemp_c")
    public double temp;

    @SerializedName("condition")
    public Condition condition;

    @SerializedName("maxwind_kph")
    public double wind;

    @SerializedName("avghumidity")
    public int humidity;

    @SerializedName("totalprecip_mm")
    public double precipitation;
}
