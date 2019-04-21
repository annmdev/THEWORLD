package com.example.r.theworld.presentation.adapter;

import android.content.res.AssetManager;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.r.theworld.R;
import com.example.r.theworld.presentation.loader.AssetsData;
import com.example.r.theworld.presentation.model.DayWeatherForecast;
import com.example.r.theworld.presentation.model.Forecast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.ItemViewHolder> {

    private List<DayWeatherForecast> list = new ArrayList<>();

    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private static SimpleDateFormat dayOfWeekFormat = new SimpleDateFormat("EE");
    private static SimpleDateFormat dayFormat = new SimpleDateFormat("MMM d");

    private AssetsData assets;

    public ForecastAdapter(AssetsData assets){
        this.assets = assets;
    }

    public void setData(List<DayWeatherForecast> list){
        this.list = list;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ItemViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_forecast, viewGroup, false), assets);

    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder itemViewHolder, int i) {
        itemViewHolder.onBind(list.get(i));
    }

    @Override
    public int getItemCount() {
        return list.size()-1;
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder{

        private TextView temp;
        private TextView dayOfWeek;
        private TextView date;
        private TextView desc;
        private TextView humidity;
        private ImageView icon;

        private AssetsData assets;

        public ItemViewHolder(@NonNull View itemView, AssetsData assets) {
            super(itemView);

            temp = itemView.findViewById(R.id.temp_forecast);
            dayOfWeek = itemView.findViewById(R.id.day_of_week);
            date = itemView.findViewById(R.id.date);
            desc = itemView.findViewById(R.id.desc_forecast);
            humidity = itemView.findViewById(R.id.humidity_fc);
            icon = itemView.findViewById(R.id.icon_desc_forecast);

            this.assets = assets;
        }

        void onBind(DayWeatherForecast data){
            String tempStr = ((int) Math.round(data.day.mintemp)) + "°.. " +
                    ((int) Math.round(data.day.maxtemp)) + "°C";
            temp.setText(tempStr);

            try {
                Date date = dateFormat.parse(data.date);
                dayOfWeek.setText(dayOfWeekFormat.format(date).toUpperCase());
                this.date.setText(dayFormat.format(date).toUpperCase());
                Log.d("rrrrrr", "onBind: " + dayFormat.format(date).toUpperCase());
            } catch (ParseException ex){}

            desc.setText(data.day.condition.description);
            icon.setImageDrawable(assets.getDrawable(data.day.condition.icon.substring(16)));
            humidity.setText(data.day.humidity + "%");
        }
    }
}
