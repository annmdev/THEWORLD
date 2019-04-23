package com.example.r.theworld.presentation.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.r.theworld.R;
import com.example.r.theworld.presentation.favorites.FavoritesDatabase;
import com.example.r.theworld.presentation.loader.AssetsData;
import com.example.r.theworld.presentation.loader.WeatherLoader;
import com.example.r.theworld.presentation.model.WeatherResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;

public class FavAdapter extends RecyclerView.Adapter<FavAdapter.WeatherItemViewHolder> {

    private ArrayList<String> list = new ArrayList<>();
    private Listener listener;
    private static AssetsData assetsData;
    private Context context;

    private WeatherItemViewHolder.NotifyItemListener notifyItemListener = new WeatherItemViewHolder.NotifyItemListener() {
        @Override
        public void notifyToRemove(int position) {
            list.remove(position);
            notifyItemRemoved(position);

            if (list.size() == 0) {
                listener.onEmptyList();
            }
        }
    };

    public FavAdapter(Listener l, AssetsData data, Context context) {
        listener = l;
        assetsData = data;
        this.context = context;
    }

    public void addList(List<String> list) {
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public WeatherItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new WeatherItemViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(
                R.layout.item_fav, viewGroup, false), listener, notifyItemListener, context);
    }

    @Override
    public void onBindViewHolder(@NonNull WeatherItemViewHolder weatherItemViewHolder, int i) {
        weatherItemViewHolder.bind(list.get(i));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class WeatherItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView location;
        private ImageView deleteBtn;
        private FrameLayout curWeatherFrameLt;
        private RelativeLayout curWeather;
        private RecyclerView forecastListRv;

        private WeatherLoader weatherLoader;

        private TextView mainTemp;
        private TextView description;
        private ProgressBar progressBar;
        private ImageView icon;
        private TextView localTime;
        private TextView wind;
        private TextView humidity;

        private RecyclerView recyclerView;
        private ForecastAdapter forecastAdapter;

        private String loc;
        private Listener listener;
        private NotifyItemListener notifyItemListener;

        private View.OnClickListener onItemClickListener;

        WeatherItemViewHolder(@NonNull final View itemView, final Listener listener,
                              final NotifyItemListener notifyItemListener, Context context) {
            super(itemView);

            this.listener = listener;
            this.notifyItemListener = notifyItemListener;

            location = itemView.findViewById(R.id.location);
            deleteBtn = itemView.findViewById(R.id.delete);
            curWeatherFrameLt = itemView.findViewById(R.id.fav_layout_cur_weather);
            curWeather = itemView.findViewById(R.id.cur_weather);
            forecastListRv = itemView.findViewById(R.id.rv_forecast_fav);
            recyclerView = itemView.findViewById(R.id.rv_forecast_fav);
            mainTemp = itemView.findViewById(R.id.temp_main);
            description = itemView.findViewById(R.id.description);
            progressBar = itemView.findViewById(R.id.progress_bar);
            itemView.findViewById(R.id.add_to_favorites).setVisibility(View.INVISIBLE);
            icon = itemView.findViewById(R.id.icon_desc);
            localTime = itemView.findViewById(R.id.cur_data);
            wind = itemView.findViewById(R.id.wind);
            humidity = itemView.findViewById(R.id.humidity);

            if (weatherLoader == null) {
                weatherLoader = new WeatherLoader(new WeatherLoader.Listener() {
                    @Override
                    public void setData(Response<WeatherResponse> data) {
                        WeatherItemViewHolder.this.setData(data.body());
                    }

                    @Override
                    public void onUnsuccessfulCall() {
                        curWeatherFrameLt.setVisibility(View.GONE);
                        forecastListRv.setVisibility(View.GONE);
                    }
                });
            }

            onItemClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (forecastListRv.getVisibility() == View.GONE) {
                        if (mainTemp.getText() == null || mainTemp.getText().length() == 0) {
                            weatherLoader.loadWeather(loc);
                            progressBar.setVisibility(View.VISIBLE);
                        }
                        curWeatherFrameLt.setVisibility(View.VISIBLE);
                        forecastListRv.setVisibility(View.VISIBLE);
                    } else {
                        curWeatherFrameLt.setVisibility(View.GONE);
                        forecastListRv.setVisibility(View.GONE);
                    }
                }
            };

            deleteBtn.setOnClickListener(this);
            itemView.findViewById(R.id.layout_city).setOnClickListener(onItemClickListener);

            forecastAdapter = new ForecastAdapter(assetsData);
            recyclerView.setAdapter(forecastAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));

        }

        void bind(final String loc) {
            this.loc = loc;
            location.setText(loc);

        }

        void setData(WeatherResponse data) {
            if (curWeather.getVisibility() == View.INVISIBLE){
                curWeather.setVisibility(View.VISIBLE);
            }
            progressBar.setVisibility(View.GONE);
            mainTemp.setText(((int) Math.round(data.current.temp_c)) + "°C");
            description.setText(data.current.condition.description);
            localTime.setText(data.location.localTime);
            wind.setText(data.current.wind + " km/h");
            humidity.setText(data.current.humidity + "%");

            icon.setImageDrawable(assetsData.getDrawable(data.current.condition.icon.substring(16)));

            forecastAdapter.setData(data.forecast.forecastList);
        }

        @Override
        public void onClick(View v) {
            Log.d("aaaaaaawww", "onClick: " + getAdapterPosition());
            listener.onDeleteClicked(location.getText().toString());
            notifyItemListener.notifyToRemove(getLayoutPosition());
        }

        public interface NotifyItemListener {
            void notifyToRemove(int position);
        }

    }

    public interface Listener {
        void onDeleteClicked(String place);

        void onEmptyList();

        WeatherResponse loadWeather(String place);
    }
}
