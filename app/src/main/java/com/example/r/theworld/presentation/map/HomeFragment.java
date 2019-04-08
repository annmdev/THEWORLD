package com.example.r.theworld.presentation.map;

import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.r.theworld.R;
import com.example.r.theworld.presentation.common.BaseMapFragment;
import com.example.r.theworld.presentation.favorites.FavoritesDatabase;
import com.example.r.theworld.presentation.model.WeatherResponse;
import com.example.r.theworld.presentation.loader.WeatherLoader;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import retrofit2.Response;

public class HomeFragment extends BaseMapFragment {

    private Marker marker;
    private BottomSheetBehavior bottomSheetBehavior;
    private FavoritesDatabase favoritesDatabase;

    private TextView mainTemp;
    private TextView place;
    private TextView description;
    private RelativeLayout shortWeatherInfoLayout;
    private ProgressBar progressBar;
    private CheckBox favorites;
    private TextView noInfoTV;

    private WeatherLoader weatherLoader;
    private String location;

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_home;
    }

    @Override
    protected void onBindView(View view, Bundle savedInstanceState) {
        super.onBindView(view, savedInstanceState);
        bottomSheetBehavior = BottomSheetBehavior.from(
                (LinearLayout) view.findViewById(R.id.design_bottom_sheet));
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

        favoritesDatabase = FavoritesDatabase.create(getContext());

        initViews(view);

        if (weatherLoader == null) {
            weatherLoader = new WeatherLoader(new WeatherLoader.Listener() {
                @Override
                public void setData(Response<WeatherResponse> data) {
                    location = data.body().location.name;
                    mainTemp.setText(data.body().current.temp + "deg");
                    description.setText(data.body().current.condition.description);
                    place.setText(location + ", " + data.body().location.country);

                    favorites.setChecked(favoritesDatabase.contains(location));

                    visible();
                }

                @Override
                public void onUnsuccessfulCall() {
                    Log.d("chchchchh", "onUnsuccessfulCall: ");
                    noInfoTV.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                }
            });
        }

        favorites.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    favoritesDatabase.put(location);
                } else {
                    favoritesDatabase.delete(location);
                }

                Log.d("favorites", "onMapClick: " + location);
            }
        });

    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        super.onMapReady(googleMap);
        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {

                if (marker != null) {
                    marker.remove();
                }

                if (progressBar.getVisibility() == View.GONE) {
                    invisible();
                }

                Log.d("favorites", "onMapClick: " + latLng.latitude + "    " + latLng.longitude);

                weatherLoader.cancelCall();

                marker = googleMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_finish)));
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

                weatherLoader.loadWeather(latLng.latitude, latLng.longitude);


            }
        });

    }

    private void visible(){
        progressBar.setVisibility(View.GONE);
        shortWeatherInfoLayout.setVisibility(View.VISIBLE);
    }

    private void invisible(){
        if (noInfoTV.getVisibility() == View.VISIBLE){
            noInfoTV.setVisibility(View.GONE);
        }
        progressBar.setVisibility(View.VISIBLE);
        shortWeatherInfoLayout.setVisibility(View.GONE);
    }

    private void initViews(View view) {
        shortWeatherInfoLayout = view.findViewById(R.id.weather_info_layout);
        mainTemp = view.findViewById(R.id.temp_main);
        place = view.findViewById(R.id.place);
        description = view.findViewById(R.id.description);
        progressBar = view.findViewById(R.id.progress_bar);
        favorites = view.findViewById(R.id.add_to_favorites);
        noInfoTV = view.findViewById(R.id.no_info_tv);
    }

}
