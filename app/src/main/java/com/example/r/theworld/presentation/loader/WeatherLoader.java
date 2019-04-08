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
        call = weatherService.getApi().getWeatherForecast(lat + ", " + lng, 5);
        call.enqueue(new retrofit2.Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                if (response.isSuccessful()){
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
