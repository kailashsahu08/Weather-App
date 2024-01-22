package com.example.weatherapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import android.util.Log;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;

import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private SearchView searchView;
    private TextView temperature,cityname,maxtemp,mintemp,day,date,mainCondition,humidity;
    private TextView windspeed,condition,sunrise,sunset,sea;
    private ConstraintLayout body;
    private LottieAnimationView lottieAnimationView;
    private String nameOfCity="bhubaneswar";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeValue();
        fetchWeatherData(nameOfCity);
        searchCity();
    }

    private void searchCity() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                fetchWeatherData(s);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return true;
            }
        });
    }

    private void initializeValue() {
        temperature = findViewById(R.id.temperature);
        cityname = findViewById(R.id.cityname);
        maxtemp = findViewById(R.id.maxtemp);
        mintemp = findViewById(R.id.mintemp);
        day = findViewById(R.id.day);
        date = findViewById(R.id.date);
        mainCondition = findViewById(R.id.mainCondition);
        humidity = findViewById(R.id.humidity);
        windspeed = findViewById(R.id.windspeed);
        condition = findViewById(R.id.condition);
        sunrise = findViewById(R.id.sunrise);
        sunset = findViewById(R.id.sunset);
        sea = findViewById(R.id.sea);
        body = findViewById(R.id.body_background);
        lottieAnimationView = findViewById(R.id.lottieAnimationView);
        searchView = findViewById(R.id.searchView);
    }

    private void fetchWeatherData(String nameOfCity) {
        //connection with retrofit
        RetrofitInstance.getInstance().api.getCurrentWeather(nameOfCity,"put your own uid","metric")
                .enqueue(new Callback<CurrentWeatherResponse>() {
            @Override
            public void onResponse(Call<CurrentWeatherResponse> call, Response<CurrentWeatherResponse> response) {
                CurrentWeatherResponse cwr = response.body();
                if(cwr!=null){
                    double temp = cwr.getMain().getTemp() ;
                    double maxtemp1 = cwr.getMain().getTempMax() ;
                    double mintemp1 = cwr.getMain().getTempMin();
                    double seaLevel1 = cwr.getMain().getSeaLevel();
                    int humidity1 = cwr.getMain().getHumidity();
                    int sunrisem = cwr.getSys().getSunrise();
                    String sunrise1 = getSunTime((long)sunrisem);
                    int sunsetm = cwr.getSys().getSunset();
                    String sunset1 = getSunTime((long)sunsetm);
                    double windspeed1 = cwr.getWind().getSpeed();
                    String weather1 = cwr.getWeather().get(0).getMain();
                    if(weather1==null){
                        weather1 = "Unknown";
                    }
                    temperature.setText(String.format("%.2f", temp)+"°C");
                    maxtemp.setText("Max "+String.format("%.2f", maxtemp1)+"°C");
                    mintemp.setText("Min "+String.format("%.2f", mintemp1)+"°C");
                    sea.setText(String.valueOf(seaLevel1));
                    humidity.setText(String.valueOf(humidity1));
                    sunrise.setText(sunrise1);
                    sunset.setText(String.valueOf(sunset1));
                    windspeed.setText(String.valueOf(windspeed1));
                    mainCondition.setText(weather1);
                    condition.setText(weather1);
                    String date1 = getDate();
                    date.setText(date1);
                    String day1 = getDay();
                    day.setText(day1);
                    cityname.setText(nameOfCity);
                    changeImageAccordingToWeather(weather1);
                }
            }

            @Override
            public void onFailure(Call<CurrentWeatherResponse> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Failed To Fatch Data", Toast.LENGTH_SHORT).show();
            }
        });
    }



    private void changeImageAccordingToWeather(String condition) {

        if(condition.equalsIgnoreCase("clear") ){
            body.setBackgroundResource(R.drawable.sunny_sky);
            lottieAnimationView.setAnimation(R.raw.sun);
        }
        else if(condition.equalsIgnoreCase("clouds")){
            body.setBackgroundResource(R.drawable.colud_background);
            lottieAnimationView.setAnimation(R.raw.cloud);
        }
        else if(condition.equalsIgnoreCase("Drizzle") ){
            body.setBackgroundResource(R.drawable.rain_background);
            lottieAnimationView.setAnimation(R.raw.drizzel);
        }
        else if(condition.equalsIgnoreCase("rain")){
            body.setBackgroundResource(R.drawable.rain_background);
            lottieAnimationView.setAnimation(R.raw.rain);
        }
        else if(condition.equalsIgnoreCase("Thunderstorm")){
            body.setBackgroundResource(R.drawable.thunderstrom);
            lottieAnimationView.setAnimation(R.raw.thunderstrom);
        }
        else if(condition.equalsIgnoreCase("snow")){
            body.setBackgroundResource(R.drawable.snow_background);
            lottieAnimationView.setAnimation(R.raw.snow);
        }
        else{
            body.setBackgroundResource(R.drawable.snowimage);
            lottieAnimationView.setAnimation(R.raw.snow2);
        }
        lottieAnimationView.playAnimation();
    }

    private String getDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM YYYY");
        return  sdf.format(new Date());
    }
    private String getSunTime(long time) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        return  sdf.format(new Date(time*1000));
    }

    private String getDay() {
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
        return  sdf.format(new Date());
    }
}