package com.example.r.theworld.presentation.loader;

import com.example.r.theworld.presentation.data.WeatherService;
import com.example.r.theworld.presentation.model.WeatherResponse;

import retrofit2.Call;
import retrofit2.Response;

public class WeatherLoader {

    private Listener listener;
    private WeatherService weatherService = new WeatherService();
    private Call call;

    public WeatherLoader(Listener listener){
        this.listener = listener;
    }

    public void loadWeather(double lat, double lng){
        loadWeather(lat + ", " + lng);
    }

    public void loadWeather(String location){
        call = weatherService.getApi().getWeatherForecast(location, 5);
        call.enqueue(new retrofit2.Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                if (response.isSuccessful()){
                    response.body().current.temp = (int) Math.floor(response.body().current.temp_c);
                    listener.setData(response);
                } else listener.onUnsuccessfulCall();
            }

            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {

            }
        });
    }

    public void cancelCall(){
        if (call != null) {
            call.cancel();
        }
    }

    public interface Listener{
        void setData(Response<WeatherResponse> data);

        void onUnsuccessfulCall();
    }
}
