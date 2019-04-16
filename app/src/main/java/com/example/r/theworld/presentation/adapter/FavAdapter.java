package com.example.r.theworld.presentation.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.r.theworld.R;

import java.util.ArrayList;
import java.util.List;

public class FavAdapter extends RecyclerView.Adapter<FavAdapter.WeatherItemViewHolder> {

    private ArrayList<String> list = new ArrayList<>();
    private Listener listener;

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

    public FavAdapter(Listener listener){
        this.listener = listener;
    }

    public void addList(List<String> list){
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public WeatherItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new WeatherItemViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(
                R.layout.item_fav, viewGroup, false), listener, notifyItemListener);
    }

    @Override
    public void onBindViewHolder(@NonNull WeatherItemViewHolder weatherItemViewHolder, int i) {
        weatherItemViewHolder.bind(list.get(i));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class WeatherItemViewHolder extends RecyclerView.ViewHolder implements CompoundButton.OnCheckedChangeListener {

        private TextView location;
        private CheckBox checkBox;

        private Listener listener;
        private NotifyItemListener notifyItemListener;

        WeatherItemViewHolder(@NonNull View itemView, Listener listener,
                              NotifyItemListener notifyItemListener) {
            super(itemView);

            this.listener = listener;
            location = itemView.findViewById(R.id.location);
            checkBox = itemView.findViewById(R.id.chk_fv);
            checkBox.setOnCheckedChangeListener(this);
            this.notifyItemListener = notifyItemListener;
        }

        void bind(String loc){
            location.setText(loc);
            checkBox.setChecked(true);
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (!isChecked) {
                listener.onChecked(location.getText().toString());
                notifyItemListener.notifyToRemove(getAdapterPosition());
            }
        }

        public interface NotifyItemListener{
            void notifyToRemove(int position);
        }

    }

    public interface Listener {
        void onChecked(String place);

        void onEmptyList();
    }
}
