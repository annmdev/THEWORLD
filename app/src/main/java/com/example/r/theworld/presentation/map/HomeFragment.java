package com.example.r.theworld.presentation.map;

import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.support.v7.widget.SearchView;
import android.widget.TextView;

import com.example.r.theworld.R;
import com.example.r.theworld.presentation.common.BaseMapFragment;
import com.example.r.theworld.presentation.favorites.FavoritesDatabase;
import com.example.r.theworld.presentation.loader.AssetsData;
import com.example.r.theworld.presentation.model.WeatherResponse;
import com.example.r.theworld.presentation.loader.WeatherLoader;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import retrofit2.Response;

public class HomeFragment extends BaseMapFragment {

    private Marker marker;
    private BottomSheetBehavior bottomSheetBehavior;
    private FavoritesDatabase favoritesDatabase;
    private AssetsData assetsData;

    private TextView mainTemp;
    private TextView place;
    private TextView description;
    private RelativeLayout shortWeatherInfoLayout;
    private ProgressBar progressBar;
    private CheckBox favorites;
    private TextView noInfoTV;
    private Toolbar toolbar;
    private ImageView icon;
    private TextView localTime;
    private TextView wind;
    private TextView humidity;

    private WeatherLoader weatherLoader;
    private WeatherLoader searchLoader;
    private String location;

    private GoogleMap googleMap;

    public static HomeFragment newInstance() {
        Log.d("78ahf9ash98a", "newInstance: ");
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
        inflateMenu();

        toolbar.setTitle("Map");

        if (weatherLoader == null) {
            weatherLoader = new WeatherLoader(new WeatherLoader.Listener() {
                @Override
                public void setData(Response<WeatherResponse> data) {
                    HomeFragment.this.setData(data.body());
                }

                @Override
                public void onUnsuccessfulCall() {
                    Log.d("chchchchh", "onUnsuccessfulCall: ");
                    shortWeatherInfoLayout.setVisibility(View.GONE);
                    noInfoTV.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                }
            });
        }

        if (searchLoader == null) {
            searchLoader = new WeatherLoader(new WeatherLoader.Listener() {
                @Override
                public void setData(Response<WeatherResponse> data) {
                    LatLng latLng = new LatLng(data.body().location.lat, data.body().location.lon);
                    HomeFragment.this.setOnMarker(latLng);
                    HomeFragment.this.goToPosition(latLng);

                    HomeFragment.this.setData(data.body());
                }

                @Override
                public void onUnsuccessfulCall() {
                    Log.d("chchchchh", "onUnsuccessfulCall: ");
                    if (marker != null) marker.remove();
                    shortWeatherInfoLayout.setVisibility(View.GONE);
                    noInfoTV.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                }
            });
        }
        favorites.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (!favoritesDatabase.contains(location)) {
                        favoritesDatabase.put(location);
                    }
                } else {
                    favoritesDatabase.delete(location);
                }
            }
        });

        if (assetsData == null){
            assetsData = new AssetsData(getActivity().getAssets());
        }

    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        super.onMapReady(googleMap);

        this.googleMap = googleMap;

        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {

                searchLoader.cancelCall();
                weatherLoader.cancelCall();
                HomeFragment.this.setOnMarker(latLng);
                HomeFragment.this.setSearchingStateOnBottom();

                weatherLoader.loadWeather(latLng.latitude, latLng.longitude);

            }
        });
        try {
            googleMap.setMyLocationEnabled(true);
        } catch (SecurityException ex) {

        }

        googleMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {
                return false;
            }
        });

        googleMap.setOnMyLocationClickListener(new GoogleMap.OnMyLocationClickListener() {
            @Override
            public void onMyLocationClick(@NonNull Location location) {
                double v1 = location.getLatitude();
                double v2 = location.getLongitude();

                LatLng latLng = new LatLng(v1, v2);

                setOnMarker(latLng);
                setSearchingStateOnBottom();
                goToPosition(latLng);

                weatherLoader.loadWeather(v1, v2);
            }
        });
    }

    private void goToPosition(LatLng latLng) {
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 3));
    }

    private void visible() {
        progressBar.setVisibility(View.GONE);
        shortWeatherInfoLayout.setVisibility(View.VISIBLE);
    }

    private void invisible() {
        if (noInfoTV.getVisibility() == View.VISIBLE) {
            noInfoTV.setVisibility(View.GONE);
        }
        place.setText("");
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
        toolbar = view.findViewById(R.id.toolbar);
        icon = view.findViewById(R.id.icon_desc);
        localTime = view.findViewById(R.id.cur_data);
        wind = view.findViewById(R.id.wind);
        humidity = view.findViewById(R.id.humidity);
    }

    private void inflateMenu() {
        toolbar.inflateMenu(R.menu.menu_toolbar);

        MenuItem searchItem = toolbar.getMenu().findItem(R.id.search_item);
        final SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                searchView.clearFocus();
                HomeFragment.this.setSearchingStateOnBottom();
                searchLoader.loadWeather(query);

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

    }

    private void setSearchingStateOnBottom() {

        if (progressBar.getVisibility() == View.GONE) {
            invisible();
        }

        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

    }

    private void setOnMarker(LatLng latLng) {
        if (marker != null) {
            marker.remove();
        }


        marker = googleMap.addMarker(new MarkerOptions()
                .position(latLng)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_finish)));
    }

    private void setData(WeatherResponse data) {
        noInfoTV.setVisibility(View.GONE);
        location = data.location.name;
        mainTemp.setText(data.current.temp + "°C");
        description.setText(data.current.condition.description);
        place.setText(location + ", " + data.location.country);
        localTime.setText(data.location.localTime);
        wind.setText(data.current.wind  + " km/h");
        humidity.setText(data.current.humidity + "%");

        icon.setImageDrawable(assetsData.getDrawable(data.current.condition.icon.substring(16)));
        favorites.setChecked(favoritesDatabase.contains(location));

        visible();
    }

}
