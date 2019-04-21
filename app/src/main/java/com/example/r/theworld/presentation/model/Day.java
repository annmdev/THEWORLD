package com.example.r.theworld.presentation.model;

import com.google.gson.annotations.SerializedName;

public class Day {

    @SerializedName("mintemp_c")
    public double mintemp;

    @SerializedName("maxtemp_c")
    public double maxtemp;

    @SerializedName("condition")
    public Condition condition;

    @SerializedName("maxwind_kph")
    public double wind;

    @SerializedName("avghumidity")
    public int humidity;

    @SerializedName("totalprecip_mm")
    public double precipitation;
}
