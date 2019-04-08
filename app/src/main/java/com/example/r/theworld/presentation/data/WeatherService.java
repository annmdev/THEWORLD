package com.example.r.theworld.presentation.data;

import com.example.r.theworld.presentation.Const;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WeatherService {

    private Retrofit retrofit;
    private OkHttpClient okHttpClient;
    private WeatherAPI weatherAPI;

    public WeatherService(){
        okHttpClient = new OkHttpClient.Builder()
                .addNetworkInterceptor(provideInterceptor())
                .addInterceptor(new AuthAInterceptor())
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(Const.WEATHER_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();

        weatherAPI = retrofit.create(WeatherAPI.class);
    }

    private HttpLoggingInterceptor provideInterceptor() {
        return new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);
    }

    public WeatherAPI getApi(){
        return weatherAPI;
    }
}
