package com.example.weather_app;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class WeatherRVAdapter extends RecyclerView.Adapter<WeatherRVAdapter.ViewHolder> {
    private Context context;
    private ArrayList<WeatherRVModal> WeatherRVModalarrayList;

    public WeatherRVAdapter(Context context, ArrayList<WeatherRVModal> weatherRVModalarrayList) {
        this.context = context;
        this.WeatherRVModalarrayList = weatherRVModalarrayList;
    }

    @NonNull
    @Override
    public WeatherRVAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.weather_rv_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WeatherRVAdapter.ViewHolder holder, int position) {
        WeatherRVModal modal = WeatherRVModalarrayList.get(position);
        holder.temperatureTV.setText(modal.getTemperature() + "°C");

        // Load the condition icon into an ImageView
        String iconUrl = "http:".concat(modal.getIcon());
        if (!iconUrl.isEmpty()) {
            Picasso.get().load(iconUrl).into(holder.conditionIV); // Using conditionIV (ImageView) now
        } else {
            Log.e("WeatherRVAdapter", "Icon URL is empty");
        }

        // Load the weather image into the weatherImageView
        String imageUrl = modal.getImageUrl();
        if (holder.weatherImageView != null && !imageUrl.isEmpty()) {
            Picasso.get().load(imageUrl).into(holder.weatherImageView);
        } else {
            Log.e("WeatherRVAdapter", "ImageView is null or Image URL is empty");
        }

        holder.windTV.setText(modal.getWindspeed() + "Km/h");

        // Handle time formatting
        SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        SimpleDateFormat output = new SimpleDateFormat("hh:mm aa");
        try {
            Date t = input.parse(modal.getTime());
            holder.timeTv.setText(output.format(t));
        } catch (ParseException e) {
            e.printStackTrace();
            holder.timeTv.setText("N/A");
        }
    }

    @Override
    public int getItemCount() {
        return WeatherRVModalarrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView windTV, temperatureTV, timeTv;
        private ImageView conditionIV, weatherImageView; // Changed conditionTV to conditionIV (ImageView)

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            windTV = itemView.findViewById(R.id.idTVWindSpeed);
            temperatureTV = itemView.findViewById(R.id.idTVTemperature);
            timeTv = itemView.findViewById(R.id.idTVTime);
            conditionIV = itemView.findViewById(R.id.idIVCondition); // Correct ImageView for weather icon
            weatherImageView = itemView.findViewById(R.id.idIVWeatherImage); // Correct ImageView for weather image
        }
    }
}
