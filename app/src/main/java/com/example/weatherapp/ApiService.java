package com.example.weatherapp;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {
    @GET("weather")
    Call<CurrentWeatherResponse> getCurrentWeather(
            @Query("q") String q,
            @Query("appid") String appId,
            @Query("units") String units

    );
}
