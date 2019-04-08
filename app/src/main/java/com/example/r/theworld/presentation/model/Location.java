package com.example.r.theworld.presentation.model;

import com.google.gson.annotations.SerializedName;

public class Location {

    @SerializedName("name")
    public String name;

    @SerializedName("country")
    public String country;

    @SerializedName("lat")
    public double lat;

    @SerializedName("lon")
    public double lon;

    @SerializedName("localtime")
    public String localTime;
}
