package com.example.weather_app;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.squareup.picasso.Picasso;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private RelativeLayout homeRL;
    private ProgressBar loadingPB;
    private TextView cityNameTV, temperatureTV, conditionTV;
    private RecyclerView weatherRV;
    private TextInputEditText cityEdt;
    private ImageView backIV, iconIV, searchIV;
    private ArrayList<WeatherRVModal> weatherRVModalArrayList;
    private WeatherRVAdapter weatherRVAdapter;
    private LocationManager locationManager;
    private int PERMISSION_CODE = 1;
    private String cityName;
    private TextView humidityTV, windTV, pressureTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialization
        homeRL = findViewById(R.id.idRLHome);
        loadingPB = findViewById(R.id.idPBLoading);
        cityNameTV = findViewById(R.id.idTVCityName);
        temperatureTV = findViewById(R.id.idTVTemperature);
        conditionTV = findViewById(R.id.idTVCondition);
        weatherRV = findViewById(R.id.idRvWeather);
        cityEdt = findViewById(R.id.idEdtCity);
        backIV = findViewById(R.id.idIVBlack);
        iconIV = findViewById(R.id.idIVIcon);
        searchIV = findViewById(R.id.idIVSearch);
        // Inside onCreate method
        humidityTV = findViewById(R.id.idTVHumidity);
        windTV = findViewById(R.id.idTVWind);
        pressureTV = findViewById(R.id.idTVPresure);


        weatherRVModalArrayList = new ArrayList<>();
        weatherRVAdapter = new WeatherRVAdapter(this, weatherRVModalArrayList);
        weatherRV.setAdapter(weatherRVAdapter);

        // Location Manager for getting the user's location
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        // Check for location permissions
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_CODE);
        } else {
            getLocation();
        }

        // Search city weather on click
        searchIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cityName = cityEdt.getText().toString();
                if (!cityName.isEmpty()) {
                    getWeatherInfo(cityName);
                } else {
                    Toast.makeText(MainActivity.this, "Please enter city name", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Method to get the current location of the user
    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        if (location != null) {
            Geocoder geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
            try {
                List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                cityName = addresses.get(0).getLocality();
                getWeatherInfo(cityName);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // Method to fetch weather information from OpenWeatherMap API
    private void getWeatherInfo(String cityName) {
        String url = "https://api.openweathermap.org/data/2.5/weather?q=" + cityName + "&units=metric&appid=4498450826d95a9f48e7ad5d43a09b87";
        cityNameTV.setText(cityName);
        // Create a request queue for network calls
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);

        // Make a request to the API
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                loadingPB.setVisibility(View.GONE);
                homeRL.setVisibility(View.VISIBLE);
                weatherRVModalArrayList.clear();

                try {
                    // Extract the required data
                    JSONObject main = response.getJSONObject("main");
                    temperatureTV.setText(main.getString("temp") + "°C");
                    // Get humidity and pressure
                    String humidity = main.getString("humidity") + "%";
                    String pressure = main.getString("pressure") + " hPa"; // Additional parameter
                    humidityTV.setText(  humidity);
                    pressureTV.setText( pressure);

                    JSONArray weatherArray = response.getJSONArray("weather");
                    JSONObject weatherObj = weatherArray.getJSONObject(0);
                    String condition = weatherObj.getString("description");
                    conditionTV.setText(condition);

                    // Load weather icon
                    String icon = weatherObj.getString("icon");
                    Picasso.get().load("https://openweathermap.org/img/wn/" + icon + "@2x.png").into(iconIV);

                    // Get wind speed
                    JSONObject wind = response.getJSONObject("wind");
                    String windSpeed = wind.getString("speed") + " m/s";
                    windTV.setText( windSpeed);

                    // Load weather icon
//String icon = weatherObj.getString("icon");

                    // Get sunrise, sunset, and dt (current time)
                    long sunrise = response.getJSONObject("sys").getLong("sunrise");
                    long sunset = response.getJSONObject("sys").getLong("sunset");
                    long currentTime = response.getLong("dt");

                    // Check if current time is day or night
                    if (currentTime >= sunrise && currentTime < sunset) {
                        // Daytime background
                    //  Picasso.get().load("https://chitrabhumi.com/wp-content/uploads/2023/01/Good-Morning-Images-with-Sun-Sunrays-25.jpg?v=1673182125").into(backIV);

                         homeRL.setBackgroundResource(R.drawable.image1);

                    } else {
                        // Nighttime background
                     //   Picasso.get().load("https://chitrabhumi.com/wp-content/uploads/2023/01/Good-Morning-Images-with-Sun-Sunrays-25.jpg?v=1673182125").into(backIV);

                        homeRL.setBackgroundResource(R.drawable.image2);
                    }
//
//                    int isDay = response.getJSONObject("current").getInt("is_day");
//
//                    if (isDay==1){
//                        Picasso.get().load("https://chitrabhumi.com/wp-content/uploads/2023/01/Good-Morning-Images-with-Sun-Sunrays-25.jpg?v=1673182125").into(backIV);
//                    }else {
//                        Picasso.get().load("https://chitrabhumi.com/wp-content/uploads/2023/01/Good-Morning-Images-with-Sun-Sunrays-25.jpg?v=1673182125").into(backIV);
//                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loadingPB.setVisibility(View.GONE);
                Toast.makeText(MainActivity.this, "Failed to get weather data", Toast.LENGTH_SHORT).show();
            }
        });

        // Add request to the queue
        requestQueue.add(jsonObjectRequest);
    }

    // Handle the permission request result
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLocation();
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
 }
}
}