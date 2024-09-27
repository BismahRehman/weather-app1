package com.example.weather_app;
import android.annotation.SuppressLint;
import android.content.Context;
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

public class WeatherRVAdapter extends RecyclerView.Adapter<WeatherRVAdapter.ViewHolder>{
    private Context context;
    private ArrayList<WeatherRVModal> WeatherRVModalarrayList;



    public WeatherRVAdapter(Context context, ArrayList<WeatherRVModal> weatherRVModalarrayList) {
        this.context = context;
        this.WeatherRVModalarrayList = weatherRVModalarrayList;
    }

    @NonNull
    @Override
    public WeatherRVAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.weather_rv_item,parent,false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull WeatherRVAdapter.ViewHolder holder, int position) {

      WeatherRVModal modal= WeatherRVModalarrayList.get(position);
      holder.temperatureTV.setText(modal.getTemperature()+"°C");
        Picasso.get().load("http:".concat(modal.getIcon())).into(holder.conditionTV);
        holder.windTV.setText(modal.getWindspeed()+"Km/h");
        SimpleDateFormat input= new SimpleDateFormat("YYYY-MM-dd hh:mm");
        SimpleDateFormat output= new SimpleDateFormat("hh:mm aa");
        try{
            Date t = input.parse(modal.getTime());
            holder.timeTv.setText(output.format(t));

        }catch (ParseException e){
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {

        return WeatherRVModalarrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView windTV, temperatureTV, timeTv;
        private ImageView conditionTV;


        @SuppressLint("WrongViewCast")
        public ViewHolder(@NonNull View itemView){
            super((itemView));
            windTV= itemView.findViewById(R.id.idTVWindSpeed);
            temperatureTV= itemView.findViewById(R.id.idTVTemperature);
            timeTv= itemView.findViewById(R.id.idTVTime);
            conditionTV=itemView.findViewById(R.id.idTVCondition);


        }
    }
}
